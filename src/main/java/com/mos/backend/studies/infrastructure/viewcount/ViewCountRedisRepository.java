package com.mos.backend.studies.infrastructure.viewcount;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ViewCountRedisRepository implements ViewCountRepository{

    private final RedisTemplate<String, String> redisTemplate;

    private static final String VIEW_COUNT_KEY = "VIEW::STUDY::%s";
    private static final long VIEW_COUNT_TTL = 600;

    @Override
    public void saveViewRecord(Long studyId, String ipAddress) {
        redisTemplate.opsForSet().add(generateKey(studyId), ipAddress);
        redisTemplate.expire(generateKey(studyId), VIEW_COUNT_TTL, TimeUnit.SECONDS);
    }

    @Override
    public boolean existsByStudyIdAndIpAddress(Long studyId, String ipAddress) {
        Boolean isMember = redisTemplate.opsForSet().isMember(generateKey(studyId), ipAddress);
        return Boolean.TRUE.equals(isMember);
    }

    private String generateKey(Long studyId) {
        return String.format(VIEW_COUNT_KEY, studyId);
    }
}
