package com.reviewping.coflo.message;

public record MrEvalResponseMessage(Long mrInfoId, MrEvaluationMessage mrEvaluationMessage, String username) {
    public record MrEvaluationMessage(
            Integer readabilityScore,
            Integer consistencyScore,
            Integer reusabilityScore,
            Integer reliabilityScore,
            Integer securityScore,
            Integer maintainabilityScore) {}
}
