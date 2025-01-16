package com.reviewping.coflo.message;

import com.reviewping.coflo.message.ReviewRequestMessage.MrContent;

public record MrEvalRequestMessage(Long mrInfoId, Long branchId, MrContent mrContent, String username) {}
