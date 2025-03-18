package com.mos.backend.studyschedules.application.res;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyCurriculumRes {
    private Long studyCurriculumId;
    private Long sectionId;
    private String title;
    private String content;

    public static StudyCurriculumRes from(StudyCurriculum studyCurriculum) {
        return new StudyCurriculumRes(
                studyCurriculum.getId(),
                studyCurriculum.getSectionId(),
                studyCurriculum.getTitle(),
                studyCurriculum.getContent()
        );
    }
}
