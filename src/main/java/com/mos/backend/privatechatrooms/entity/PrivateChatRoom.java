package com.mos.backend.privatechatrooms.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "private_chat_rooms")
public class PrivateChatRoom extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "private_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "requester_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private PrivateChatRoomStatus status;

    public static PrivateChatRoom createInvisibleChatRoom(User requester, User receiver) {
        PrivateChatRoom privateChatRoom = new PrivateChatRoom();
        privateChatRoom.requester = requester;
        privateChatRoom.receiver = receiver;
        privateChatRoom.status = PrivateChatRoomStatus.INVISIBLE;
        return privateChatRoom;
    }

    public void visible() {
        status = PrivateChatRoomStatus.VISIBLE;
    }
}
