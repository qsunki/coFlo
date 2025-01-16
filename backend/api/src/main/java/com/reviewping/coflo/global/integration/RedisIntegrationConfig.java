package com.reviewping.coflo.global.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;
import org.springframework.integration.redis.outbound.RedisPublishingMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan(basePackages = "com.reviewping.coflo")
public class RedisIntegrationConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public MessageChannel redisInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel redisOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel reviewResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel detailedReviewResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mrEvalResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    public RedisInboundChannelAdapter redisInboundChannelAdapter(
            @Qualifier("redisInboundChannel") MessageChannel redisInboundChannel) {
        RedisInboundChannelAdapter adapter = new RedisInboundChannelAdapter(redisConnectionFactory);
        adapter.setTopics(
                "test", "init", "review-response", "mr-eval-response", "detailed-review-response");
        adapter.setOutputChannel(redisInboundChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "redisOutboundChannel")
    public MessageHandler redisOutboundAdapter() {
        RedisPublishingMessageHandler handler =
                new RedisPublishingMessageHandler(redisConnectionFactory);
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression topicExpression = parser.parseExpression("headers['topic']");
        handler.setTopicExpression(topicExpression);
        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        handler.setSerializer(serializer);
        return handler;
    }

    @ServiceActivator(inputChannel = "redisInboundChannel")
    @Bean
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("redis_messageSource");
        router.setChannelMapping("test", "testInboundChannel");
        router.setChannelMapping("review-response", "reviewResponseChannel");
        router.setChannelMapping("detailed-review-response", "detailedReviewResponseChannel");
        router.setChannelMapping("mr-eval-response", "mrEvalResponseChannel");
        return router;
    }
}
