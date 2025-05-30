package com.mos.backend.studies.application.responsedto;

import com.mos.backend.studies.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudyCategoriesResponseDto {
    private List<String> categories;

}
