package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyMemberRepositoryImpl implements StudyMemberRepository {

    private final StudyMemberJpaRepository studyMemberJpaRepository;


    @Override
    public StudyMember save(StudyMember studyMember) {
        return studyMemberJpaRepository.save(studyMember);
    }

    @Override
    public long countByStudy(Study study) {
        return studyMemberJpaRepository.countByStudy(study);
    }
}
