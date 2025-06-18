package com.mos.backend.studymaterials.application.responsedto;

import com.mos.backend.studymaterials.entity.StudyMaterial;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

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
        responseDto.createdAt = studyMaterial.getCreatedAt();
        responseDto.modifiedAt = studyMaterial.getModifiedAt();
        return responseDto;
    }
}
