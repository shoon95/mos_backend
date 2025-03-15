package com.mos.backend.questionanswers.infrastructure;

import com.mos.backend.common.EntitySaver;
import com.mos.backend.questionanswers.entity.QuestionAnswer;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyjoins.application.res.QuestionAnswerRes;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import com.mos.backend.testconfig.AbstractTestContainer;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionAnswerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    private EntitySaver entitySaver;

    @Test
    @DisplayName("studyJoinId를 통한 QuestionAnswerRes 조회")
    void findAllByStudyJoinId() {
        // given
        User user1 = entitySaver.saveUser();
        User user2 = entitySaver.saveUser();

        Study study = entitySaver.saveStudy();
        StudyJoin studyJoin1 = entitySaver.saveStudyJoin(user1, study, StudyJoinStatus.PENDING);
        StudyJoin studyJoin2 = entitySaver.saveStudyJoin(user2, study, StudyJoinStatus.PENDING);

        StudyQuestion studyQuestion1 = entitySaver.saveStudyQuestion(study, 1L);
        StudyQuestion studyQuestion2 = entitySaver.saveStudyQuestion(study, 2L);
        StudyQuestion studyQuestion3 = entitySaver.saveStudyQuestion(study, 3L);

        QuestionAnswer questionAnswer1 = entitySaver.saveQuestionAnswer(studyJoin1, studyQuestion1);
        QuestionAnswer questionAnswer2 = entitySaver.saveQuestionAnswer(studyJoin1, studyQuestion2);
        QuestionAnswer questionAnswer3 = entitySaver.saveQuestionAnswer(studyJoin1, studyQuestion3);
        // 다른 유저의 답변
        QuestionAnswer questionAnswer4 = entitySaver.saveQuestionAnswer(studyJoin2, studyQuestion3);


        // when
        List<QuestionAnswerRes> questionAnswerResList = questionAnswerRepository.findAllByStudyJoinId(studyJoin1.getId());

        // then
        assertThat(questionAnswerResList).hasSize(3);
        assertThat(questionAnswerResList).extracting("studyQuestionId")
                .containsExactlyInAnyOrder(studyQuestion1.getId(), studyQuestion2.getId(), studyQuestion3.getId());
        assertThat(questionAnswerResList).extracting("questionAnswerId")
                .containsExactlyInAnyOrder(questionAnswer1.getId(), questionAnswer2.getId(), questionAnswer3.getId());
        assertThat(questionAnswerResList).extracting("questionNum")
                .containsExactly(1L, 2L, 3L);
    }


}
