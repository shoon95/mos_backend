package com.mos.backend.common.auth;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.userschedules.entity.UserSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userScheduleSecurity")
@RequiredArgsConstructor
public class UserScheduleSecurity {
    private final EntityFacade entityFacade;

    public boolean isScheduleOwnerOrAdmin(long userScheduleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        if (currentUser.isAdmin())
            return true;

        UserSchedule userSchedule = entityFacade.getUserSchedule(userScheduleId);
        return userSchedule.getUser().isOwner(currentUserId);
    }

    private static void isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MosException(UserErrorCode.USER_UNAUTHORIZED);
        }
    }
}
