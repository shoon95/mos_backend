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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyNoticeService {

    private final StudyNoticeRepository studyNoticeRepository;
    private final UserService userService;
    private final EntityFacade entityFacade;

    /**
     * 공지 생성
     */

    @Transactional
    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    public StudyNoticeResponseDto create(long studyId, long currentUserId, String title, String content, boolean pinned, boolean important) {
        // 중요 공지로 설정했을 때 중요 공지가 이미 존재하다면 기존 중요 공지 마크 해제
        if (important) {
            studyNoticeRepository.findByStudyIdAndImportantIsTrue(studyId).ifPresent(StudyNotice::unmarkAsImportant);
        }

        Study study = entityFacade.getStudy(studyId);
        User currentUser = entityFacade.getUser(currentUserId);

        StudyNotice studyNotice = StudyNotice.create(study, currentUser, title, content, pinned, important);
        StudyNotice savedStudyNotice = studyNoticeRepository.save(studyNotice);
        return StudyNoticeResponseDto.of(savedStudyNotice, currentUser, currentUser);
    }

    /**
     * 공지 수정
     */

    @Transactional
    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    public StudyNoticeResponseDto update(long studyId, long studyNoticeId, String title, String content, boolean pinned, boolean important) {
        // 중요 공지로 설정했을 때 중요 공지가 이미 존재하다면 기존 중요 공지 마크 해제
        if (important) {
            studyNoticeRepository.findByStudyIdAndImportantIsTrue(studyId).ifPresent(StudyNotice::unmarkAsImportant);
        }

        StudyNotice studyNotice = findByIdWithUser(studyNoticeId);
        studyNotice.update(title, content, pinned, important);

        User creator = studyNotice.getUser();
        User modifier = entityFacade.getUser(studyNotice.getUpdatedBy());
        return StudyNoticeResponseDto.of(studyNotice, creator, modifier);
    }

    /**
     * 공지 삭제
     */

    @Transactional
    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    public void delete(long studyId, long studyNoticeId) {
        studyNoticeRepository.deleteById(studyNoticeId);
    }

    /**
     * 공지 단 건 조회
     */

    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    public StudyNoticeResponseDto readOne(long studyId, long studyNoticeId) {
        StudyNotice studyNotice = findByIdWithUser(studyNoticeId);
        User creator = studyNotice.getUser();
        User modifier = entityFacade.getUser(studyNotice.getUpdatedBy());
        return StudyNoticeResponseDto.of(studyNotice, creator, modifier);
    }

    /**
     * 공지 다 건 조회
     */
    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    public List<StudyNoticeResponseDto> readAll(long studyId) {
        List<StudyNotice> studyNoticeList = studyNoticeRepository.findAllByStudyId(studyId);

        // 유저 조회 쿼리 최적화를 위해 중복 제거 후 한 번에 조회
        Set<Long> modifierIdSet = studyNoticeList.stream()
                .map(StudyNotice::getUpdatedBy)
                .collect(Collectors.toSet());

        Map<Long, User> modifierMap = userService.findAllById(modifierIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return studyNoticeList.stream().map(sn -> {
            User creator = sn.getUser();
            User modifier = modifierMap.get(sn.getUpdatedBy());
            return StudyNoticeResponseDto.of(sn, creator, modifier);
        }).toList();
    }


    private StudyNotice findByIdWithUser(long studyNoticeId) {
        return studyNoticeRepository.findById(studyNoticeId)
                .orElseThrow(() -> new MosException(StudyNoticeErrorCode.STUDY_NOTICE_NOT_EXISTS));
    }
}
