package com.mos.backend.users.infrastructure.respository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final UserJpaRepository userJpaRepository;
    private final UserRedisRepository userRedisRepository;
}
