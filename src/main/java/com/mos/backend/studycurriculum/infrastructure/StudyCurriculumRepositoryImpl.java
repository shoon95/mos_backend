package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyCurriculumRepositoryImpl implements StudyCurriculumRepository {

    private final StudyCurriculumJpaRepository studyCurriculumJpaRepository;

    @Override
    public void saveAll(List<StudyCurriculum> studyCurriculumList) {
        studyCurriculumJpaRepository.saveAll(studyCurriculumList);
    }
}
