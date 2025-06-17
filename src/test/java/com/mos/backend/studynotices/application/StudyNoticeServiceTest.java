package com.mos.backend.studynotices.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studynotices.application.responsedto.StudyNoticeResponseDto;
import com.mos.backend.studynotices.entity.StudyNotice;
import com.mos.backend.studynotices.entity.StudyNoticeErrorCode;
import com.mos.backend.studynotices.infrastructure.StudyNoticeRepository;
import com.mos.backend.users.application.UserService;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyNoticeServiceTest {

    @InjectMocks
    private StudyNoticeService studyNoticeService;

    @Mock
    private StudyNoticeRepository studyNoticeRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityFacade entityFacade;


    @Test
    @DisplayName("공지를 성공적으로 생성한다.")
    void create_success() {
        // given
        Long studyId = 1L;
        Long currentUserId = 1L;

        String title = "테스트 공지 제목";
        String content = "테스트 공지 내용";
        boolean pinned = true;
        boolean important = true;

        Study study = mock(Study.class);
        User user = mock(User.class);
        doReturn(currentUserId).when(user).getId();
        StudyNotice studyNotice = mock(StudyNotice.class);
        doReturn(title).when(studyNotice).getTitle();
        doReturn(content).when(studyNotice).getContent();
        doReturn(pinned).when(studyNotice).isPinned();
        doReturn(important).when(studyNotice).isImportant();

        when(entityFacade.getStudy(studyId)).thenReturn(study);
        when(entityFacade.getUser(currentUserId)).thenReturn(user);
        when(studyNoticeRepository.save(any(StudyNotice.class))).thenReturn(studyNotice);

        // when
        StudyNoticeResponseDto studyNoticeResponseDto = studyNoticeService.create(studyId, currentUserId, title, content, pinned, important);

        // then
        assertThat(studyNoticeResponseDto.getStudyId()).isEqualTo(studyId);
        assertThat(studyNoticeResponseDto.getCreatorId()).isEqualTo(currentUserId);
        assertThat(studyNoticeResponseDto.getTitle()).isEqualTo(title);
        assertThat(studyNoticeResponseDto.getContent()).isEqualTo(content);
        assertThat(studyNoticeResponseDto.isPinned()).isEqualTo(pinned);
        assertThat(studyNoticeResponseDto.isImportant()).isEqualTo(important);
    }

    @Test
    @DisplayName("중요 공지 생성 시, 기존 중요 공지는 해제된다.")
    void whenCreateImportantNotice_thenUnmarksOldOne() {

        // given
        Long studyId = 1L;
        Long currentUserId = 1L;

        String title = "테스트 공지 제목";
        String content = "테스트 공지 내용";
        boolean pinned = true;
        boolean important = true;

        Study study = mock(Study.class);
        doReturn(studyId).when(study).getId();
        User user = mock(User.class);
        doReturn(currentUserId).when(user).getId();
        StudyNotice studyNotice = mock(StudyNotice.class);
        doReturn(title).when(studyNotice).getTitle();
        doReturn(content).when(studyNotice).getContent();
        doReturn(pinned).when(studyNotice).isPinned();
        doReturn(important).when(studyNotice).isImportant();

        StudyNotice oldImportantNotice = StudyNotice.create(study, user, title, content, pinned, important);

        when(studyNoticeRepository.findByStudyIdAndImportantIsTrue(study.getId()))
                .thenReturn(Optional.of(oldImportantNotice));
        when(entityFacade.getStudy(studyId)).thenReturn(study);
        when(entityFacade.getUser(currentUserId)).thenReturn(user);
        when(studyNoticeRepository.save(any(StudyNotice.class))).thenReturn(studyNotice);

        // when
        studyNoticeService.create(studyId, currentUserId, "new title", "new content", false, true);

        // then
        assertThat(oldImportantNotice.isImportant()).isFalse(); // 기존 공지가 해제되었는지 확인
        verify(studyNoticeRepository).save(any(StudyNotice.class));
    }

    @Test
    @DisplayName("공지를 성공적으로 수정한다")
    void update_success() {
        // given
        Long studyId = 1L;
        Long studyNoticeId = 1L;

        Long creatorId = 1L;
        String creatorNickname = "creator";

        Long modifierId = 1L;
        String modifierNickname = "modifier";

        String title = "테스트 공지 제목";
        String content = "테스트 공지 내용";
        boolean pinned = true;
        boolean important = true;

        String updateTitle = "업데이트 제목";
        String updateContent = "업데이트 내용";
        boolean updatePinned = false;
        boolean updateImportant = false;

        Study study = mock(Study.class);
        User creator = mock(User.class);
        User modifier = mock(User.class);

        StudyNotice studyNotice = StudyNotice.create(study, creator, title, content, pinned, important);

        doReturn(modifierNickname).when(modifier).getNickname();

        doReturn(creatorId).when(creator).getId();
        doReturn(creatorNickname).when(creator).getNickname();

        StudyNotice originalStudyNotice = spy(studyNotice);
        doReturn(modifierId).when(originalStudyNotice).getUpdatedBy();
        doReturn(studyNoticeId).when(originalStudyNotice).getId();

        when(studyNoticeRepository.findById(studyNoticeId)).thenReturn(Optional.of(originalStudyNotice));
        when(entityFacade.getUser(modifierId)).thenReturn(modifier);

        // when
        StudyNoticeResponseDto updatedNotice = studyNoticeService.update(studyId, studyNoticeId, updateTitle, updateContent, updatePinned, updateImportant);

        // then
        assertThat(updatedNotice.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedNotice.getContent()).isEqualTo(updateContent);
        assertThat(updatedNotice.isPinned()).isEqualTo(updatePinned);
        assertThat(updatedNotice.isImportant()).isEqualTo(updateImportant);
        assertThat(updatedNotice.getModifierId()).isEqualTo(modifierId);
        assertThat(updatedNotice.getModifierNickname()).isEqualTo(modifierNickname);
    }

    @Test
    @DisplayName("공지 수정 시 존재하지 않으면 mosException이 발생한다.")
    void givenNotExistsNoticeId_WhenUpdateNotice_ThenMosException() {
        // given
        Long studyId = 1L;
        Long studyNoticeId = 1L;

        String updateTitle = "업데이트 제목";
        String updateContent = "업데이트 내용";
        boolean updatePinned = false;
        boolean updateImportant = false;

        when(studyNoticeRepository.findById(studyNoticeId)).thenReturn(Optional.empty());

        // when
        MosException mosException = assertThrows(MosException.class, () -> studyNoticeService.update(studyId, studyNoticeId, updateTitle, updateContent, updatePinned, updateImportant));

        // then
        assertThat(mosException.getErrorCode()).isEqualTo(StudyNoticeErrorCode.STUDY_NOTICE_NOT_EXISTS);
    }
}