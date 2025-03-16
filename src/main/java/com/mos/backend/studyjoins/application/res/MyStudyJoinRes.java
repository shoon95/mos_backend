package com.mos.backend.studyjoins.application.res;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyjoins.entity.StudyJoin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyStudyJoinRes {
    private Long studyId;
    private String title;
    private String category;

    private Long studyJoinId;
    private String studyJoinStatus;

    public static MyStudyJoinRes from(StudyJoin studyJoin) {
        Study study = studyJoin.getStudy();
        return new MyStudyJoinRes(study.getId(), study.getTitle(), study.getCategory().getDescription(), studyJoin.getId(), studyJoin.getStatus().getDescription());
    }
}
