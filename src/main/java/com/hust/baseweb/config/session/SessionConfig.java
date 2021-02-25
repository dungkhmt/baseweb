package com.hust.baseweb.config.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        return RedisSerializationBuilder.getDefaultRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public RedisOperations<String, Object> redisOperations() {
        return RedisSerializationBuilder.getSnappyRedisTemplate(redisConnectionFactory(), Object.class);
    }

    @Bean
    public RedisSessionRepository sessionRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisSessionRepository(redisTemplate);
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
