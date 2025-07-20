package com.mos.backend.userstudylikes.application;

import com.mos.backend.common.event.Event;
import com.mos.backend.common.event.EventType;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import com.mos.backend.userstudylikes.application.event.StudyLikeEventPayload;
import com.mos.backend.userstudylikes.application.response.UserStudyLikeResponseDto;
import com.mos.backend.userstudylikes.entity.UserStudyLike;
import com.mos.backend.userstudylikes.infrastructure.UserStudyLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStudyLikeService {

    private final UserStudyLikeRepository userStudyLikeRepository;
    private final EntityFacade entityFacade;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public UserStudyLikeResponseDto like(Long studyId, Long userId) {
        Study study = entityFacade.getStudy(studyId);
        User user = entityFacade.getUser(userId);
        if (!existsUserStudyLike(user, study)) {
            userStudyLikeRepository.save(UserStudyLike.create(user, study));
            eventPublisher.publishEvent(new Event<>(EventType.STUDY_LIKED, new StudyLikeEventPayload(HotStudyEventType.LIKE, studyId)));
        }
        return new UserStudyLikeResponseDto(userId, studyId, userStudyLikeRepository.getLikedCountByStudyId(studyId));
    }

    @Transactional
    public UserStudyLikeResponseDto unlike(Long studyId, Long userId) {
        Study study = entityFacade.getStudy(studyId);
        User user = entityFacade.getUser(userId);
        if (existsUserStudyLike(user, study)) {
            userStudyLikeRepository.deleteByUserAndStudy(user, study);
            eventPublisher.publishEvent(new Event<>(EventType.STUDY_LIKE_CANCELED, new StudyLikeEventPayload(HotStudyEventType.LIKE_CANCEL, studyId)));
        }
        return new UserStudyLikeResponseDto(userId, studyId, userStudyLikeRepository.getLikedCountByStudyId(studyId));
    }

    /**
     * 스터디의 좋아요 수 조회
     */
    public Long getLikedCountByStudyId(Long studyId) {
        return userStudyLikeRepository.getLikedCountByStudyId(studyId);
    }

    /**
     * 현재 유저가 특정 스터디를 좋아요 했는지 확인
     */
    public boolean isLikedByMe(Long studyId, Long currentUserId) {
        if (currentUserId == null) {
            return false;
        }
        return userStudyLikeRepository.isLikedByMe(studyId, currentUserId);
    }

    private boolean existsUserStudyLike(User user, Study study) {
        return userStudyLikeRepository.existsUserStudyLikeByUserAndStudy(user, study);
    }



}
