package com.mos.backend.studymaterials.application.responsedto;

import com.mos.backend.studymaterials.entity.StudyMaterial;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReadStudyMaterialResponseDto {
    private Long id;
    private Long studyId;
    private Long studyMemberId;
    private Long userId;
    private String userNickname;
    private String filePath;
    private String originalName;
    private Long fileSize;

    public static ReadStudyMaterialResponseDto from(StudyMaterial studyMaterial) {
        ReadStudyMaterialResponseDto responseDto = new ReadStudyMaterialResponseDto();
        responseDto.id = studyMaterial.getId();
        responseDto.studyId = studyMaterial.getStudy().getId();
        responseDto.studyMemberId = studyMaterial.getStudyMember().getId();
        responseDto.userId = studyMaterial.getStudyMember().getUser().getId();
        responseDto.userNickname = studyMaterial.getStudyMember().getUser().getNickname();
        responseDto.filePath = studyMaterial.getFilePath();
        responseDto.originalName = studyMaterial.getOriginalName();
        responseDto.fileSize = studyMaterial.getFileSize();
        return responseDto;
    }
}
