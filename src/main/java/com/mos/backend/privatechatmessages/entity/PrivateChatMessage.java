package com.mos.backend.privatechatmessages.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "private_chat_messages")
public class PrivateChatMessage extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "private_chat_message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "private_chat_room_id")
    private PrivateChatRoom privateChatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public static PrivateChatMessage of(User user, PrivateChatRoom privateChatRoom, String message) {
        PrivateChatMessage privateChatMessage = new PrivateChatMessage();
        privateChatMessage.user = user;
        privateChatMessage.privateChatRoom = privateChatRoom;
        privateChatMessage.message = message;
        return privateChatMessage;
    }
}
