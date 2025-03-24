package com.mos.backend.studyrequirements.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrequirements.entity.StudyRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyRequirementRepositoryImpl implements StudyRequirementRepository{

    private final StudyRequirementJpaRepository studyRequirementJpaRepository;

    @Override
    public List<StudyRequirement> findAllByStudy(Study study) {
        return studyRequirementJpaRepository.findAllByStudy(study);
    }

    @Override
    public void deleteAll(List<StudyRequirement> deleteRequirementList) {
        studyRequirementJpaRepository.deleteAll(deleteRequirementList);
    }

    @Override
    public void save(StudyRequirement studyRequirement) {
        studyRequirementJpaRepository.save(studyRequirement);
    }

    @Override
    public Optional<StudyRequirement> findByIdAndStudy(Long id, Study study) {
        return studyRequirementJpaRepository.findByIdAndStudy(id, study);
    }
}
