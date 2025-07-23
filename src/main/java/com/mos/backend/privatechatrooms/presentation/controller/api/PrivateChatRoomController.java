package com.mos.backend.privatechatrooms.presentation.controller.api;

import com.mos.backend.privatechatrooms.application.PrivateChatRoomService;
import com.mos.backend.privatechatrooms.application.res.MyPrivateChatRoomRes;
import com.mos.backend.privatechatrooms.presentation.req.PrivateChatRoomCreateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PrivateChatRoomController {
    private final PrivateChatRoomService privateChatRoomService;

    private static final String BASE_URL = "/private-chat-rooms/";

    @PostMapping("/private-chat-rooms")
    public ResponseEntity<Void> create(@AuthenticationPrincipal Long userId, @RequestBody PrivateChatRoomCreateReq privateChatRoomCreateReq) {
        Long privateChatRoomId = privateChatRoomService.create(userId, privateChatRoomCreateReq);
        return ResponseEntity.created(URI.create(BASE_URL + privateChatRoomId)).build();
    }

    @GetMapping("/private-chat-rooms/search")
    public Long getPrivateChatRoomId(@AuthenticationPrincipal Long userId, @RequestParam Long counterpartId) {
        return privateChatRoomService.getPrivateChatRoomId(userId, counterpartId);
    }

    @GetMapping("/private-chat-rooms")
    public List<MyPrivateChatRoomRes> getMyPrivateChatRooms(@AuthenticationPrincipal Long userId) {
        return privateChatRoomService.getMyPrivateChatRooms(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/private-chat-rooms/{privateChatRoomId}")
    public void enter(@AuthenticationPrincipal Long userId, @PathVariable Long privateChatRoomId) {
        privateChatRoomService.enter(userId, privateChatRoomId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/private-chat-rooms/{privateChatRoomId}")
    public void leave(@AuthenticationPrincipal Long userId, @PathVariable Long privateChatRoomId) {
        privateChatRoomService.leave(userId, privateChatRoomId);
    }

}
