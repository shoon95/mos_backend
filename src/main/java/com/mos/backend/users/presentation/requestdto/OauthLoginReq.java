package com.mos.backend.users.presentation.requestdto;

import com.mos.backend.users.entity.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OauthLoginReq {
    private String code;
    private OauthProvider oauthProvider;
}
