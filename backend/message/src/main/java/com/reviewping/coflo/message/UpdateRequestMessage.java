package com.reviewping.coflo.message;

public record UpdateRequestMessage(
        Long projectId, Long branchId, String gitUrl, String branch, String token, String commitHash) {}
