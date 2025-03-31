package com.mos.backend.studymaterials.application.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReadAllStudyMaterialResponseDto {
    private int count;
    private Long totalFileSize;
    private List<ReadStudyMaterialResponseDto> fileList;

    public static ReadAllStudyMaterialResponseDto of(Long totalStudySizeBytes, List<ReadStudyMaterialResponseDto> studyMaterialList) {
        ReadAllStudyMaterialResponseDto responseDto = new ReadAllStudyMaterialResponseDto();
        responseDto.count = studyMaterialList.size();
        responseDto.totalFileSize = totalStudySizeBytes;
        responseDto.fileList = studyMaterialList;
        return responseDto;
    }
}
