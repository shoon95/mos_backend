package com.mos.backend.chatmessages.entity;

import com.mos.backend.chatparticipants.entity.ChatParticipant;
import com.mos.backend.chatrooms.entity.ChatRoom;
import com.mos.backend.common.entity.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_messages")
public class ChatMessage extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "chat_participant_id")
    private ChatParticipant chatParticipant;
}
