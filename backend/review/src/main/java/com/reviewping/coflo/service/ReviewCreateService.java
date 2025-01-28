package com.reviewping.coflo.service;

import com.reviewping.coflo.gateway.RedisGateway;
import com.reviewping.coflo.message.*;
import com.reviewping.coflo.message.ReviewRequestMessage.MrContent;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.ChatCompletionResponse;
import com.reviewping.coflo.openai.dto.EmbeddingResponse;
import com.reviewping.coflo.repository.ChunkedCodeRepository;
import com.reviewping.coflo.repository.PromptTemplateRepository;
import com.reviewping.coflo.service.dto.ChunkedCode;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewCreateService {

    private static final String REVIEW_TYPE = "REVIEW";
    private static final String DETAILED_REVIEW_TYPE = "DETAILED_REVIEW";

    private final RedisGateway redisGateway;
    private final OpenaiClient openaiClient;
    private final PromptTemplateRepository promptTemplateRepository;
    private final ChunkedCodeRepository chunkedCodeRepository;

    public ReviewCreateService(
            RedisGateway redisGateway,
            OpenaiClient openaiClient,
            PromptTemplateRepository promptTemplateRepository,
            ChunkedCodeRepository chunkedCodeRepository) {
        this.redisGateway = redisGateway;
        this.openaiClient = openaiClient;
        this.promptTemplateRepository = promptTemplateRepository;
        this.chunkedCodeRepository = chunkedCodeRepository;
    }

    @WithSpan
    @ServiceActivator(inputChannel = "reviewRequestChannel")
    public void createOverallReview(ReviewRequestMessage reviewRequest) {
        Long projectId = reviewRequest.projectId();
        Long branchId = reviewRequest.branchId();
        log.info(
                "전체리뷰 생성 시작 - GitLab URL: {}, MR Info ID: {}, Project ID: {}, Branch ID: {}",
                reviewRequest.gitlabUrl(),
                reviewRequest.mrInfoId(),
                projectId,
                branchId);
        // 1. mr 임베딩
        EmbeddingResponse embeddingResponse =
                openaiClient.generateEmbedding(reviewRequest.mrContent().mrDiffs());
        float[] embedding = embeddingResponse.data().getFirst().embedding();
        // 2. 참고자료 검색
        List<ChunkedCode> chunkedCodes = chunkedCodeRepository.retrieveRelevantData(projectId, branchId, 10, embedding);
        List<RetrievalMessage> retrievals =
                chunkedCodes.stream().map(this::fromChunkedCode).toList();
        // 3. 프롬프트 생성
        String prompt = buildPrompt(reviewRequest.mrContent(), reviewRequest.customPrompt(), retrievals);
        // 4. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage =
                chatCompletionResponse.choices().getFirst().message().content();
        // 5. 리뷰 생성 완료
        ReviewResponseMessage reviewResponse = new ReviewResponseMessage(
                0L, reviewRequest.gitlabUrl(), reviewRequest.mrInfoId(), chatMessage, retrievals);
        log.info(
                "전체리뷰 생성 완료 - GitLab URL: {}, MR Info ID: {}, 참고자료 수: {}",
                reviewResponse.gitlabUrl(),
                reviewResponse.mrInfoId(),
                reviewResponse.retrievals().size());
        redisGateway.sendReview(reviewResponse);
    }

    @WithSpan
    @ServiceActivator(inputChannel = "reviewRegenerateRequestChannel")
    public void regenerateReview(ReviewRegenerateRequestMessage reviewRegenerateRequest) {
        // 1. 프롬프트 생성
        log.info(
                "리뷰 재생성 시작 - GitLab URL: {}, MR Info ID: {}, 참고자료 수: {}",
                reviewRegenerateRequest.gitlabUrl(),
                reviewRegenerateRequest.mrInfoId(),
                reviewRegenerateRequest.retrievals().size());
        String prompt = buildPrompt(
                reviewRegenerateRequest.mrContent(),
                reviewRegenerateRequest.customPrompt(),
                reviewRegenerateRequest.retrievals());
        // 2. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage =
                chatCompletionResponse.choices().getFirst().message().content();
        // 3. 리뷰 생성 완료
        ReviewResponseMessage reviewResponse = new ReviewResponseMessage(
                reviewRegenerateRequest.userId(),
                reviewRegenerateRequest.gitlabUrl(),
                reviewRegenerateRequest.mrInfoId(),
                chatMessage,
                reviewRegenerateRequest.retrievals());
        log.info(
                "리뷰 재생성 완료 - GitLab URL: {}, MR Info ID: {}, 참고자료 수: {}",
                reviewResponse.gitlabUrl(),
                reviewResponse.mrInfoId(),
                reviewResponse.retrievals().size());
        redisGateway.sendReview(reviewResponse);
    }

    @WithSpan
    @ServiceActivator(inputChannel = "detailedReviewRequestChannel")
    public void createDetailedReview(DetailedReviewRequestMessage detailedReviewRequest) {
        Long projectId = detailedReviewRequest.projectId();
        Long branchId = detailedReviewRequest.branchId();
        log.info(
                "상세리뷰 생성 시작 - GitLab URL: {}, MR Info ID: {}, Project ID: {}, Branch ID: {}",
                detailedReviewRequest.gitlabUrl(),
                detailedReviewRequest.mrInfoId(),
                projectId,
                branchId);
        // 1. mr 임베딩
        EmbeddingResponse embeddingResponse = openaiClient.generateEmbedding(detailedReviewRequest.diff());
        float[] embedding = embeddingResponse.data().getFirst().embedding();
        // 2. 참고자료 검색
        List<ChunkedCode> chunkedCodes = chunkedCodeRepository.retrieveRelevantData(projectId, branchId, 10, embedding);
        List<RetrievalMessage> retrievals =
                chunkedCodes.stream().map(this::fromChunkedCode).toList();
        // 3. 프롬프트 생성
        String prompt = buildDetailedPrompt(detailedReviewRequest.diff(), retrievals);
        // 4. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage =
                chatCompletionResponse.choices().getFirst().message().content();
        // 5. 리뷰 생성 완료
        DetailedReviewResponseMessage reviewResponse = new DetailedReviewResponseMessage(
                projectId,
                detailedReviewRequest.mrInfoId(),
                branchId,
                chatMessage,
                detailedReviewRequest.baseSha(),
                detailedReviewRequest.headSha(),
                detailedReviewRequest.startSha(),
                detailedReviewRequest.newPath(),
                detailedReviewRequest.oldPath(),
                detailedReviewRequest.gitlabUrl(),
                retrievals);
        log.info(
                "상세리뷰 생성 완료 - GitLab URL: {}, MR Info ID: {}, 참고자료 수: {}",
                reviewResponse.gitlabUrl(),
                reviewResponse.mrInfoId(),
                reviewResponse.retrievals().size());
        redisGateway.sendReview(reviewResponse);
    }

    private String buildDetailedPrompt(String diff, List<RetrievalMessage> retrievals) {
        String prompt = promptTemplateRepository
                .findLatestTemplate(DETAILED_REVIEW_TYPE)
                .content();
        return prompt.formatted(retrievals, diff);
    }

    private String buildPrompt(MrContent mrContent, String customPrompt, List<RetrievalMessage> retrievals) {
        String prompt = promptTemplateRepository.findLatestTemplate(REVIEW_TYPE).content();
        return prompt.formatted(retrievals, mrContent.mrDescription(), mrContent.mrDiffs(), customPrompt);
    }

    private RetrievalMessage fromChunkedCode(ChunkedCode chunkedCode) {
        return new RetrievalMessage(chunkedCode.getContent(), chunkedCode.getFileName(), chunkedCode.getLanguage());
    }
}
