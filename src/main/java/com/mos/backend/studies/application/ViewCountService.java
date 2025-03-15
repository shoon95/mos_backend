package com.mos.backend.studies.application;

import com.mos.backend.studies.infrastructure.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ViewCountService {
    private static final String VIEW_COUNT_KEY = "VIEW::STUDY::%s";
    // TTL 10분
    private static final long VIEW_COUNT_TTL = 600;

    private final RedisTemplate<String, String> redisTemplate;
    private final StudyRepository studyRepository;

    @Transactional
    public void handleViewCount(Long studyId, String ipAddress) {
        if (isCountable(studyId, ipAddress)) {
            saveViewRecord(studyId, ipAddress);
            increaseViewCount(studyId);
        }
    }

    public boolean isCountable(Long studyId, String ipAddress) {
        Boolean isMember = redisTemplate.opsForSet().isMember(generateKey(studyId), ipAddress);
        return !Boolean.TRUE.equals(isMember);
    }


    private void saveViewRecord(Long studyId, String ipAddress) {
        redisTemplate.opsForSet().add(generateKey(studyId), ipAddress);
        redisTemplate.expire(generateKey(studyId), VIEW_COUNT_TTL, TimeUnit.SECONDS);
    }


    private String generateKey(Long studyId) {
        return String.format(VIEW_COUNT_KEY, studyId);
    }

    private void increaseViewCount(long studyId) {
        studyRepository.increaseViewCount(studyId);
    }
}
