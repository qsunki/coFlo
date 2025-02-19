package com.reviewping.coflo.global.client.gitlab.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MergeRequestDiffVersionContent(String headCommitSha, String baseCommitSha, String startCommitSha) {}
