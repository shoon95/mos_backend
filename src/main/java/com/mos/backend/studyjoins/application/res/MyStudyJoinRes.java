package com.mos.backend.studyjoins.application.res;

import com.mos.backend.studies.entity.Category;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyStudyJoinRes {
    private Long studyJd;
    private String title;
    private Category category;

    private Long studyJoinId;
    private StudyJoinStatus studyJoinStatus;

    public static MyStudyJoinRes from(StudyJoin studyJoin) {
        Study study = studyJoin.getStudy();
        return new MyStudyJoinRes(study.getId(), study.getTitle(), study.getCategory(), studyJoin.getId(), studyJoin.getStatus()
        );
    }
}
