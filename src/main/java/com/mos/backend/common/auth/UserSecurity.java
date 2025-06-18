package com.mos.backend.common.auth;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final EntityFacade entityFacade;

    /**
     * 로그인한 User와 요청 User 가 같은지 또는 로그인한 User가 관리자인지 검증
     */
    public boolean isOwnerOrAdmin(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());

        User currentUser = entityFacade.getUser(currentUserId);

        if (currentUser.isAdmin()) {
            return true;
        } else {
            return currentUser.isOwner(userId);
        }
    }

    private static void isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MosException(UserErrorCode.USER_UNAUTHORIZED);
        }
    }
}
