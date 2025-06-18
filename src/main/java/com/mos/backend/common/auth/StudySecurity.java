package com.mos.backend.common.auth;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studymaterials.entity.StudyMaterial;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("studySecurity")
@RequiredArgsConstructor
public class StudySecurity {

    private final EntityFacade entityFacade;

    /**
     * 로그인한 유저가 스터디의 리더 또는 관리자인지 검증
     */

    public boolean isLeaderOrAdmin(Long studyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());

        User user = entityFacade.getUser(currentUserId);
        if (user.isAdmin()) {
            return true;
        }

        return entityFacade.getStudyMember(currentUserId, studyId).isLeader();
    }


    /**
     * 로그인한 User가 지원자 본인인지 또는 관리자인지 검증
     */

    public boolean isApplicantOrAdmin(long studyJoinId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        StudyJoin studyJoin = entityFacade.getStudyJoin(studyJoinId);

        if (currentUser.isAdmin()) {
            return true;
        }
        return studyJoin.getUser().isOwner(currentUserId);
    }

    /**
     * 로그인한 유저가 스터디원인지 또는 관리자인지 검증
     */

    public boolean isMemberOrAdmin(long studyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        if (currentUser.isAdmin()) {
            return true;
        }
        return entityFacade.getStudyMember(currentUserId, studyId) != null;
    }

    /**
     * 로그인한 유저가 스터디장이거나 업로더 본인이거나 관리자인지 검증
     */

    public boolean isLeaderOrUploaderOrAdmin(long materialId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        StudyMaterial studyMaterial = entityFacade.getStudyMaterial(materialId);

        if (currentUser.isAdmin() || studyMaterial.getStudyMember().getUser().isOwner(currentUserId)) {
            return true;
        }
        return studyMaterial.getStudyMember().isLeader();
    }

    /**
     * 로그인한 유저가 스터디의 단순 스터디회원인지 또는 관리자인지 검증
     */

    public boolean isMemberRoleOrAdmin(long studyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        isAuthenticated(authentication);

        Long currentUserId = Long.valueOf(authentication.getName());
        User currentUser = entityFacade.getUser(currentUserId);

        if (currentUser.isAdmin()) {
            return true;
        }

        StudyMember studyMember = entityFacade.getStudyMember(currentUserId, studyId);
        if (studyMember.isLeader()) {
            throw new MosException(StudyMemberErrorCode.STUDY_LEADER_WITHDRAW_FORBIDDEN);
        }
        return true;

    }

    private static void isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MosException(UserErrorCode.USER_UNAUTHORIZED);
        }
    }
}
