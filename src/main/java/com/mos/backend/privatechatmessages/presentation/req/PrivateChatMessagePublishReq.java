package com.mos.backend.privatechatmessages.presentation.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChatMessagePublishReq {
    @NotNull(message = "메시지 내용은 Null을 허용하지 않습니다.")
    @Size(min = 1, max = 1000, message = "메시지 내용은 1자 이상 1000자 이하로 입력해주세요.")
    private String message;
}
