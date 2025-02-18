package com.reviewping.coflo.gateway;

import com.reviewping.coflo.message.DetailedReviewResponseMessage;
import com.reviewping.coflo.message.MrEvalResponseMessage;
import com.reviewping.coflo.message.ReviewResponseMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "redisOutboundChannel")
public interface RedisGateway {

    @Gateway(headers = @GatewayHeader(name = "topic", value = "review-response"))
    void sendReview(ReviewResponseMessage reviewResponse);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "detailed-review-response"))
    void sendReview(DetailedReviewResponseMessage reviewResponse);

    @Gateway(headers = @GatewayHeader(name = "topic", value = "mr-eval-response"))
    void sendEval(MrEvalResponseMessage evalResponse);
}
