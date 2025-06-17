package com.mos.backend.users.infrastructure.respository;

import com.mos.backend.users.entity.OauthProvider;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserRedisRepository userRedisRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId) {
        return userJpaRepository.findByOauthProviderAndSocialId(oauthProvider, socialId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public List<User> findAllById(Set<Long> userIds) {
        return userJpaRepository.findAllById(userIds);
    }
}
