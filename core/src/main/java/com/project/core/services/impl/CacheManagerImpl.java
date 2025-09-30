package com.project.core.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheManagerImpl {
    private final RedisTemplate<String, Object> redisTemplate;

    @Async
    public void evictCachedChatMessages(String chatId) {
        Set<String> keys = redisTemplate.keys("messages::" + chatId + "-*");
        redisTemplate.delete(keys);
    }
}
