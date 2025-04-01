package com.mos.backend.userstudylikes.application;

import com.mos.backend.common.event.Event;
import com.mos.backend.common.event.EventType;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.hotstudies.application.HotStudyService;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import com.mos.backend.userstudylikes.application.event.StudyLikeEventPayload;
import com.mos.backend.userstudylikes.entity.UserStudyLike;
import com.mos.backend.userstudylikes.infrastructure.UserStudyLikeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStudyLikeServiceTest {

    @Mock
    private UserStudyLikeRepository userStudyLikeRepository;

    @Mock
    private EntityFacade entityFacade;

    @Mock
    private HotStudyService hotStudyService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserStudyLikeService userStudyLikeService;

    @Captor
    ArgumentCaptor<Event<StudyLikeEventPayload>> likedEventCaptor;
    @Captor
    ArgumentCaptor<Event<StudyLikeEventPayload>> likeCanceledEventCaptor;

    @Nested
    @DisplayName("좋아요 기능")
    class LikeTest {

        @Test
        @DisplayName("좋아요 성공")
        void like() {

            // given
            Long studyId = 1L;
            Long userId = 1L;

            Study study = Study.builder().id(studyId).build();
            User user = User.builder().id(userId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getUser(userId)).thenReturn(user);
            when(userStudyLikeRepository.existsUserStudyLikeByUserAndStudy(user, study)).thenReturn(false);

            // when
            userStudyLikeService.like(studyId, userId);

            // then
            verify(userStudyLikeRepository).save(any(UserStudyLike.class));
            verify(eventPublisher).publishEvent(likedEventCaptor.capture());
            Event<StudyLikeEventPayload> value = likedEventCaptor.getValue();
            assertThat(value.getEventType()).isEqualTo(EventType.STUDY_LIKED);
            assertThat(value.getResolvableType().hasGenerics()).isTrue();
            assertThat(value.getResolvableType().getGeneric(0).resolve()).isEqualTo(StudyLikeEventPayload.class);
            assertThat(value.getPayload().getStudyId()).isEqualTo(studyId);
            assertThat(value.getPayload().getType()).isEqualTo(HotStudyEventType.LIKE);
        }

        @Test
        @DisplayName("좋아요 실패")
        void alreadyLiked_like_fail() {
            // given
            Long studyId = 1L;
            Long userId = 1L;

            Study study = Study.builder().id(studyId).build();
            User user = User.builder().id(userId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getUser(userId)).thenReturn(user);
            when(userStudyLikeRepository.existsUserStudyLikeByUserAndStudy(user, study)).thenReturn(true);

            // when
            userStudyLikeService.like(studyId, userId);

            // then
            verify(userStudyLikeRepository, never()).save(any(UserStudyLike.class));
            verify(eventPublisher, never()).publishEvent(likedEventCaptor.capture());
        }
    }

    @Nested
    @DisplayName("좋아요 취소 기능")
    class UnlikeTest {

        @Test
        @DisplayName("좋아요 취소 성공")
        void unlike() {

            // given
            Long studyId = 1L;
            Long userId = 1L;

            Study study = Study.builder().id(studyId).build();
            User user = User.builder().id(userId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getUser(userId)).thenReturn(user);
            when(userStudyLikeRepository.existsUserStudyLikeByUserAndStudy(user, study)).thenReturn(true);

            // when
            userStudyLikeService.unlike(studyId, userId);

            // then
            verify(userStudyLikeRepository).deleteByUserAndStudy(user, study);
            verify(eventPublisher).publishEvent(likeCanceledEventCaptor.capture());
            Event<StudyLikeEventPayload> value = likeCanceledEventCaptor.getValue();
            assertThat(value.getEventType()).isEqualTo(EventType.STUDY_LIKE_CANCELED);
            assertThat(value.getResolvableType().hasGenerics()).isTrue();
            assertThat(value.getResolvableType().getGeneric(0).resolve()).isEqualTo(StudyLikeEventPayload.class);
            assertThat(value.getPayload().getStudyId()).isEqualTo(studyId);
            assertThat(value.getPayload().getType()).isEqualTo(HotStudyEventType.LIKE_CANCEL);

        }

        @Test
        @DisplayName("좋아요 취소 실패")
        void alreadyUnliked_unlike_fail() {
            // given
            Long studyId = 1L;
            Long userId = 1L;

            Study study = Study.builder().id(studyId).build();
            User user = User.builder().id(userId).build();

            when(entityFacade.getStudy(studyId)).thenReturn(study);
            when(entityFacade.getUser(userId)).thenReturn(user);
            when(userStudyLikeRepository.existsUserStudyLikeByUserAndStudy(user, study)).thenReturn(false);

            // when
            userStudyLikeService.unlike(studyId, userId);

            // then
            verify(userStudyLikeRepository, never()).deleteByUserAndStudy(user, study);
            verify(eventPublisher, never()).publishEvent(likedEventCaptor.capture());
        }

    }

}