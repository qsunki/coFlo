package com.reviewping.coflo.message;

import com.reviewping.coflo.message.ReviewRequestMessage.MrContent;
import java.util.List;

public record ReviewRegenerateRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        Long userId,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
