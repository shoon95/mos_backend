package com.mos.backend.studyrequirements.application.responsedto;

import com.mos.backend.studyrequirements.entity.StudyRequirement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyRequirementResponseDto {
    private Long id;
    private Long requirementNum;
    private String content;

    public static StudyRequirementResponseDto from(StudyRequirement studyRequirement) {
        StudyRequirementResponseDto studyRequirementResponseDto = new StudyRequirementResponseDto();
        studyRequirementResponseDto.id = studyRequirement.getId();
        studyRequirementResponseDto.requirementNum = studyRequirement.getRequirementNum();
        studyRequirementResponseDto.content = studyRequirement.getContent();
        return studyRequirementResponseDto;
    }
}
