package com.mos.backend.users.application.responsedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mos.backend.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRes {
    private String nickname;
    private String introduction;
    private String categories;
    private String imagePath;

    public static UserDetailRes from(User user) {
        UserDetailRes userDetailRes = new UserDetailRes();
        userDetailRes.nickname = user.getNickname();
        userDetailRes.introduction = user.getIntroduction();
        userDetailRes.categories = user.getCategories();
        userDetailRes.imagePath = user.getImagePath();
        return userDetailRes;
    }
}
