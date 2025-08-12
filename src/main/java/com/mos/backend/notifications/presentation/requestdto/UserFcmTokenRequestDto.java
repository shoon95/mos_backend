package com.mos.backend.notifications.presentation.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFcmTokenRequestDto {
    private String fcmToken;
}
