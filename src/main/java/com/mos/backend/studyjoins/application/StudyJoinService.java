package com.mos.backend.studyjoins.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.questionanswers.infrastructure.QuestionAnswerRepository;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studyjoins.application.res.MyStudyJoinRes;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;
import com.mos.backend.studyjoins.application.res.StudyJoinRes;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import com.mos.backend.studyjoins.entity.exception.StudyJoinErrorCode;
import com.mos.backend.studyjoins.infrastructure.StudyJoinRepository;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.exception.StudyMemberErrorCode;
import com.mos.backend.studymembers.infrastructure.StudyMemberRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

    private final StudyJoinRepository studyJoinRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final EntityFacade entityFacade;

    @Transactional(readOnly = true)
    public List<StudyJoinRes> getStudyJoins(Long userId, Long studyId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        List<StudyJoin> studyJoins = studyJoinRepository.findAllByStudyId(study.getId());

        return studyJoins.stream()
                .map(studyJoin -> {
                    List<QuestionAnswerRes> questionAnswers = questionAnswerRepository.findAllByStudyJoinId(studyJoin.getId());
                    return StudyJoinRes.of(studyJoin, questionAnswers);
                })
                .toList();
    }

    @Transactional
    public void approveStudyJoin(Long userId, Long studyId, Long studyJoinId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudyJoin studyJoin = entityFacade.getStudyJoin(studyJoinId);

        validateSameStudy(studyJoin, study);

        long studyMemberCnt = studyMemberRepository.countByStudy(study);
        validateStudyMemberCnt(studyMemberCnt, study);

        handleApprove(studyJoin, study, user);
    }

    @Transactional
    public void rejectStudyJoin(Long userId, Long studyId, Long studyJoinId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudyJoin studyJoin = entityFacade.getStudyJoin(studyJoinId);

        validateSameStudy(studyJoin, study);

        studyJoin.reject();
    }

    @Transactional
    public void cancelStudyJoin(Long userId, Long studyId, Long studyJoinId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudyJoin studyJoin = entityFacade.getStudyJoin(studyJoinId);

        validateSameStudy(studyJoin, study);
        validatePendingStatus(studyJoin);

        studyJoin.cancel();
    }

    @Transactional(readOnly = true)
    public List<MyStudyJoinRes> getMyStudyJoins(Long userId, StudyJoinStatus status) {
        User user = entityFacade.getUser(userId);

        List<StudyJoin> studyJoins = studyJoinRepository.findAllByStatusWithStudy(status);

        return studyJoins.stream().map(MyStudyJoinRes::from).toList();
    }

    private static void validatePendingStatus(StudyJoin studyJoin) {
        if (!studyJoin.isPending())
            throw new MosException(StudyJoinErrorCode.STUDY_JOIN_NOT_PENDING);
    }

    private static void validateSameStudy(StudyJoin studyJoin, Study study) {
        if (!studyJoin.isSameStudy(study))
            throw new MosException(StudyJoinErrorCode.STUDY_JOIN_MISMATCH);
    }

    private static void validateStudyMemberCnt(long studyMemberCnt, Study study) {
        if (studyMemberCnt == 0)
            throw new MosException(StudyErrorCode.STUDY_NOT_FOUND);

        if (studyMemberCnt >= study.getMaxStudyMemberCount())
            throw new MosException(StudyMemberErrorCode.STUDY_MEMBER_FULL);
    }

    private void handleApprove(StudyJoin studyJoin, Study study, User user) {
        studyJoin.approve();
        studyMemberRepository.save(StudyMember.create(study, user));
    }
}
