package com.mos.backend.users.infrastructure.respository;

import com.mos.backend.users.entity.OauthProvider;
import com.mos.backend.users.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    User save(User user);

    Optional<User> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);

    Optional<User> findById(Long userId);

    List<User> findAllById(Set<Long> userIds);
}
