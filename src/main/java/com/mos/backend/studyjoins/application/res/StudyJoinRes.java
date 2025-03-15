package com.mos.backend.studyjoins.application.res;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StudyJoinRes {
    private Long studyJoinId;
    private LocalDateTime createdAt;

    private Long userId;
    private String nickname;

    private List<QuestionAnswerRes> questionAnswerResList;

    public static StudyJoinRes of(StudyJoin sj, List<QuestionAnswerRes> questionAnswers) {
        User user = sj.getUser();
        return new StudyJoinRes(sj.getId(), sj.getCreatedAt(), user.getId(), user.getNickname(), questionAnswers);
    }
}
