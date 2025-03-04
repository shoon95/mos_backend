package com.mos.backend.studymembers.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.users.infrastructure.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    public void create(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new MosException(UserErrorCode.USER_NOT_FOUND));

        studyMemberRepository.save(StudyMember.create(study, user));
    }
}
