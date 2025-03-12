package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.StudyMember;

public interface StudyMemberRepository {

    StudyMember save(StudyMember studyMember);
    long countByStudy(Study study);
}
