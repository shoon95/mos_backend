package com.mos.backend.common;

import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.questionanswers.infrastructure.QuestionAnswerRepository;
import com.mos.backend.studies.entity.Category;
import com.mos.backend.studies.entity.MeetingType;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import com.mos.backend.studyjoins.infrastructure.StudyJoinJpaRepository;
import com.mos.backend.studyquestions.entity.QuestionOption;
import com.mos.backend.studyquestions.entity.QuestionType;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import com.mos.backend.studyquestions.infrastructure.StudyQuestionRepository;
import com.mos.backend.users.entity.OauthProvider;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.UserRole;
import com.mos.backend.users.infrastructure.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EntitySaver {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private StudyJoinJpaRepository studyJoinJpaRepository;
    @Autowired
    private StudyQuestionRepository studyQuestionRepository;
    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    public User saveUser() {
        return userRepository.save(
                User.builder()
                        .nickname("nickname")
                        .oauthProvider(OauthProvider.KAKAO)
                        .role(UserRole.USER)
                        .socialId("1")
                        .build()
        );
    }

    public Study saveStudy() {
        return studyRepository.save(
                Study.builder()
                        .title("스터디 제목")
                        .content("스터디 내용")
                        .maxStudyMemberCount(3)
                        .category(Category.PROGRAMMING)
                        .recruitmentStartDate(LocalDate.now().minusDays(1))
                        .recruitmentEndDate(LocalDate.now().plusDays(5))
                        .color("#FFFFFF")
                        .meetingType(MeetingType.ONLINE)
                        .build()
        );
    }

    public StudyJoin saveStudyJoin(User user, Study study, StudyJoinStatus status) {
        return studyJoinJpaRepository.save(
                StudyJoin.builder()
                        .user(user)
                        .study(study)
                        .status(status)
                        .build()
        );
    }

    public StudyQuestion saveStudyQuestion(Study study, Long questionNum) {
        List<String> options = List.of("선택지1", "선택지2", "선택지3");
        return studyQuestionRepository.save(
                StudyQuestion.create(study, questionNum, "질문", "객관식", options, true)
        );
    }

    public QuestionAnswer saveQuestionAnswer(StudyJoin studyJoin, StudyQuestion studyQuestion) {
        return questionAnswerRepository.save(new QuestionAnswer(studyJoin, studyQuestion, "답변"));
    }
}
