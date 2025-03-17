package com.mos.backend.studies.infrastructure.viewcount;

import com.mos.backend.testconfig.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
class ViewCountRedisRepositoryTest extends AbstractTestContainer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ViewCountRedisRepository viewCountRedisRepository;

    private static final String VIEW_COUNT_KEY = "VIEW::STUDY::%s";
    private static final long VIEW_COUNT_TTL = 600;
    private static final Long TEST_STUDY_ID = 1L;
    private static final String TEST_IP = "127.0.0.1";
    private String key;

    @BeforeEach
    void setup() {
        key = generateKey(TEST_STUDY_ID);
        redisTemplate.delete(key);
    }

    @Test
    @DisplayName("새로운 IP 주소에 대한 조회 기록 저장")
    void saveViewRecord_newIp() {
        //when
        viewCountRedisRepository.saveViewRecord(TEST_STUDY_ID, TEST_IP);

        //then
        Set<String> members = redisTemplate.opsForSet().members(key);
        assertThat(members).containsExactly(TEST_IP);
        assertThat(redisTemplate.getExpire(key, TimeUnit.SECONDS)).isBetween(VIEW_COUNT_TTL - 10, VIEW_COUNT_TTL);
    }

    @Test
    @DisplayName("조회 기록이 존재하는 경우")
    void existsByStudyIdAndIpAddress_exists() {
        //given
        viewCountRedisRepository.saveViewRecord(TEST_STUDY_ID, TEST_IP);

        //when
        boolean exists = viewCountRedisRepository.existsByStudyIdAndIpAddress(TEST_STUDY_ID, TEST_IP);

        //then
        assertTrue(exists);
    }

    @Test
    @DisplayName("조회 기록이 존재하지 않는 경우")
    void existsByStudyIdAndIpAddress_notExists() {
        //when
        boolean exists = viewCountRedisRepository.existsByStudyIdAndIpAddress(TEST_STUDY_ID, TEST_IP);

        //then
        assertFalse(exists);
    }

    private String generateKey(Long studyId) {
        return String.format(VIEW_COUNT_KEY, studyId);
    }
}