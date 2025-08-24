package com.mos.backend.common.auth;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("studyMemberSecurity")
@RequiredArgsConstructor
public class StudyMemberSecurity {
    private final EntityFacade entityFacade;

    public boolean isMemberOrAdmin(long studyChatRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        StudyChatRoom studyChatRoom = entityFacade.getStudyChatRoom(studyChatRoomId);

        if (currentUser.isAdmin()) {
            return true;
        }
        return entityFacade.getStudyMember(currentUserId, studyChatRoom.getStudy().getId()) != null;
    }

    private static void isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MosException(UserErrorCode.USER_UNAUTHORIZED);
        }
    }
}
