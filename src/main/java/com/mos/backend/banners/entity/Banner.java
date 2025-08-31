package com.mos.backend.banners.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String imageUrl;
    private String linkUrl;

    @Column(nullable = false)
    private int sortOrder;

    @Builder
    public Banner(String title, String content, String imageUrl, String linkUrl, int sortOrder) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.sortOrder = sortOrder;
    }

    public void update(String title, String content, String imageUrl, String linkUrl, int sortOrder) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.sortOrder = sortOrder;
    }
}
