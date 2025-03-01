package com.mos.backend.chatparticipants.entity;

import com.mos.backend.chatrooms.entity.ChatRoom;
import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_participants")
public class ChatParticipant extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_participant_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "chat_room_id")
    private ChatRoom chatRoom;
}
