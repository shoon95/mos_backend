package com.mos.backend.banners.application.responsedto;

import com.mos.backend.banners.entity.Banner;
import lombok.Getter;

@Getter
public class BannerResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String linkUrl;
    private int sortOrder;

    public BannerResponseDto(Banner banner) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.content = banner.getContent();
        this.imageUrl = banner.getImageUrl();
        this.linkUrl = banner.getLinkUrl();
        this.sortOrder = banner.getSortOrder();
    }
}
