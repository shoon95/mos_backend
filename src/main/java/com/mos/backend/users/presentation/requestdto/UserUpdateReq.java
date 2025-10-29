package com.mos.backend.users.presentation.requestdto;

import com.mos.backend.studies.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateReq {
    @NotBlank(message = "nickname 필수입니다.")
    private String nickname;
    private String introduction;
    private List<Category> categories;
}
