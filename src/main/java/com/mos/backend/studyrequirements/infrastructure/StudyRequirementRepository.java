package com.mos.backend.studyrequirements.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.studyrequirements.entity.StudyRequirement;
import com.mos.backend.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudyRequirementRepository {
    List<StudyRequirement> findAllByStudy(Study study);

    void deleteAll(List<StudyRequirement> deleteRequirementList);

    void save(StudyRequirement studyRequirement);

    Optional<StudyRequirement> findByIdAndStudy(Long id, Study study);

}
