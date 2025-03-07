package com.mos.backend.studies.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Arrays;
import java.util.List;

@Embeddable
public class StudyTag {

    @Column(name = "tags", nullable = true)
    private String value;

    public List<String> toList() {
        if (this.value == null || this.value.isBlank()) {
            return List.of();
        }
        return Arrays.asList(this.value.split(","));
    }

    public static StudyTag fromList(List<String> tags) {
        StudyTag studyTag = new StudyTag();
        studyTag.value = String.join(",", tags);
        return studyTag;
    }
}
