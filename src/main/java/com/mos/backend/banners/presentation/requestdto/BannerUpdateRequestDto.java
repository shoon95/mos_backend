package com.mos.backend.banners.presentation.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BannerUpdateRequestDto {
    @NotBlank(message = "배너 제목은 필수입니다.")
    private String title;
    private String content;
    private String linkUrl;
    @NotNull(message = "배너 순서는 필수입니다.")
    @Min(value = 1, message = "배너 순서는 1 이상이어야 합니다.")
    private Integer sortOrder;
}
