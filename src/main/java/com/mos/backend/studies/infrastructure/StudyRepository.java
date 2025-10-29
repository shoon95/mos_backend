package com.mos.backend.studies.infrastructure;

import com.mos.backend.studies.application.responsedto.StudiesResponseDto;
import com.mos.backend.studies.application.responsedto.StudyResponseDto;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.application.responsedto.UserStudiesResponseDto;
import com.mos.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudyRepository {
    Study save(Study study);
    Optional<Study> findById(Long id);

    void increaseViewCount(Long studyId);

    // 스터디 다 건 조회 (카드)
    Page<StudiesResponseDto> findStudies(Long currentUserId, Pageable pageable, String categoryCond, String meetingTypeCond, String recruitmentStatusCond, String progressStatusCond, boolean liked);

    long count();

    void delete(Long studyId);

    // 유저가 참여 중인 스터디 목록 조회
    List<UserStudiesResponseDto> readUserStudies(User user, String progressStatusCond, String participationStatusCond);

    // 스터디 단 건 조회
    StudyResponseDto getStudyDetails(Long studyId, Long currentUserId);

    // 인기 스터디 상세 정보 조회
    StudiesResponseDto getHotStudyDetails(Long studyId, Long currentUserId);
}
