package com.mos.backend.users.infrastructure.respository;

import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{
    private final UserJpaRepository userJpaRepository;
    private final UserRedisRepository userRedisRepository;

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }
}
