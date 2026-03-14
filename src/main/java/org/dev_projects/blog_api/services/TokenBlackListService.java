package org.dev_projects.blog_api.services;

import lombok.RequiredArgsConstructor;
import org.dev_projects.blog_api.configurations.JwtConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtConfig jwtConfig;

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                jwtConfig.getAccessTokenExpiration(),
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
