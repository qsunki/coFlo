package com.reviewping.coflo.openai.dto;

import java.util.List;

public record ArrayEmbeddingRequest(List<String> input, String model) {}
