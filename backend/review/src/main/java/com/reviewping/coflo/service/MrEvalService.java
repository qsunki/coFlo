package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.gateway.RedisGateway;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.message.MrEvalRequestMessage;
import com.reviewping.coflo.message.MrEvalResponseMessage;
import com.reviewping.coflo.message.MrEvalResponseMessage.MrEvaluationMessage;
import com.reviewping.coflo.message.ReviewRequestMessage.MrContent;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.ChatCompletionResponse;
import com.reviewping.coflo.repository.PromptTemplateRepository;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MrEvalService {

    private static final String PROMPT_TYPE = "MR_EVAL";

    private final JsonUtil jsonUtil;
    private final OpenaiClient openaiClient;
    private final RedisGateway redisGateway;
    private final PromptTemplateRepository promptTemplateRepository;

    private String EVAL_TEMPLATE;

    @PostConstruct
    public void setEvalTemplate() {
        MrEvaluationMessage mrEvaluationMessage = new MrEvaluationMessage(0, 0, 0, 0, 0, 0);
        EVAL_TEMPLATE = jsonUtil.toJson(mrEvaluationMessage);
    }

    @WithSpan
    @ServiceActivator(inputChannel = "mrEvalRequestChannel")
    public void evaluateMr(MrEvalRequestMessage mrEvalRequest) {
        log.info("MR 평가 시작 - MR Info ID: {}", mrEvalRequest.mrInfoId());
        String evalPrompt = buildEvalPrompt(mrEvalRequest.mrContent());
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(evalPrompt);
        MrEvaluationMessage mrEvaluationMessage = jsonUtil.fromJson(
                chatCompletionResponse.choices().getFirst().message().content(), new TypeReference<>() {});
        MrEvalResponseMessage mrEvalResponseMessage =
                new MrEvalResponseMessage(mrEvalRequest.mrInfoId(), mrEvaluationMessage, mrEvalRequest.username());
        redisGateway.sendEval(mrEvalResponseMessage);
        log.info("MR 평가 완료 - MR Info ID: {}, 평가 결과: {}", mrEvalRequest.mrInfoId(), mrEvaluationMessage);
    }

    private String buildEvalPrompt(MrContent mrContent) {
        String prompt = promptTemplateRepository.findLatestTemplate(PROMPT_TYPE).content();
        return prompt.formatted(mrContent.mrDiffs(), EVAL_TEMPLATE);
    }
}
