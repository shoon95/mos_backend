package com.mos.backend.studies.infrastructure;

import com.mos.backend.studies.application.responsedto.StudiesResponseDto;
import com.mos.backend.studies.application.responsedto.StudyResponseDto;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.application.responsedto.UserStudiesResponseDto;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepository{

    private final StudyJpaRepository studyJpaRepository;
    private final StudyQueryDslRepository studyQueryDSLRepository;

    @Override
    public Study save(Study study) {
        return studyJpaRepository.save(study);
    }

    @Override
    public Optional<Study> findById(Long id) {
        return studyJpaRepository.findById(id);
    }

    @Override
    public void increaseViewCount(Long studyId) {
        studyJpaRepository.increaseViewCount(studyId);
    }

    @Override
    public Page<StudiesResponseDto> findStudies(Long currentUserId, Pageable pageable, String categoryCond, String meetingTypeCond, String recruitmentStatusCond, String progressStatusCond, boolean liked) {
        return studyQueryDSLRepository.findStudies(currentUserId, pageable, categoryCond, meetingTypeCond, recruitmentStatusCond, progressStatusCond, liked);
    }

    @Override
    public long count() {
        return studyJpaRepository.count();
    }

    @Override
    public void delete(Long studyId) {
        studyJpaRepository.deleteById(studyId);
    }

    @Override
    public List<UserStudiesResponseDto> readUserStudies(User user, String progressStatusCond, String participationStatusCond) {
        return studyQueryDSLRepository.readUserStudies(user, progressStatusCond, participationStatusCond);
    }

    @Override
    public StudyResponseDto getStudyDetails(Long studyId, Long currentUserId) {
        return studyQueryDSLRepository.getStudyDetails(studyId, currentUserId);
    }

    @Override
    public StudiesResponseDto getHotStudyDetails(Long studyId, Long currentUserId) {
        return studyQueryDSLRepository.getHotStudyDetails(studyId, currentUserId);
    }
}
