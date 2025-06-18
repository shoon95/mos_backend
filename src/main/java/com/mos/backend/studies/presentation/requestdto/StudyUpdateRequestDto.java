package com.mos.backend.studies.presentation.requestdto;

import com.mos.backend.studyrequirements.presentation.requestdto.StudyRequirementCreateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyUpdateRequestDto {

    @NotBlank(message = "title 필수입니다.")
    private String title;

    @NotBlank(message = "category 필수입니다.")
    private String category;

    @NotNull(message = "maxStudyMemberCount는 필수입니다.")
    @Min(value = 1, message = "mayStudyMemberCount는 1보다 커야합니다.")
    private Integer maxStudyMemberCount;

    @NotNull(message = "recruitmentStartDate 필수입니다.")
    @FutureOrPresent(message = "recruitmentStartDate는 오늘 이후 날짜여야 합니다.")
    private LocalDate recruitmentStartDate;

    @NotNull(message = "recruitmentEndDate 필수입니다.")
    private LocalDate recruitmentEndDate;

    @NotNull(message = "tags 필수입니다.")
    private List<String> tags;

    @NotNull(message = "meetingType 필수입니다.")
    private String meetingType;

    private String schedule;

    @NotBlank(message = "content 필수입니다.")
    private String content;

    @Valid
    private List<StudyRequirementCreateRequestDto> requirements;
}
