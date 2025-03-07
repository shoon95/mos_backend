package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studymembers.entity.StudyMember;

public interface StudyMemberRepository {

    StudyMember save(StudyMember studyMember);
}
