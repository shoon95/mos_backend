package com.mos.backend.studychatparticipants.entity;

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
@Table(name = "study_chat_participants")
public class StudyChatParticipant extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_chat_participant_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "study_chat_room_id")
    private StudyChatRoom studyChatRoom;
}
