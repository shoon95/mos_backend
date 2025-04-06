package com.mos.backend.studychatmessages.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_chat_messages")
public class StudyChatMessage extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_chat_message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_chat_room_id")
    private StudyChatRoom studyChatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public static StudyChatMessage of(User user, StudyChatRoom studyChatRoom, String message) {
        StudyChatMessage studyChatMessage = new StudyChatMessage();
        studyChatMessage.user = user;
        studyChatMessage.studyChatRoom = studyChatRoom;
        studyChatMessage.message = message;
        return studyChatMessage;
    }
}
