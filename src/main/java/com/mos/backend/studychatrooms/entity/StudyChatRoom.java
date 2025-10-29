package com.mos.backend.studychatrooms.entity;

import com.mos.backend.studies.entity.Study;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_chat_rooms")
public class StudyChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_chat_room_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "study_id")
    @OnDelete(action = CASCADE)
    private Study study;

    @Column(nullable = false)
    private String name;

    public static StudyChatRoom create(Study study, String name) {
        StudyChatRoom studyChatRoom = new StudyChatRoom();
        studyChatRoom.study = study;
        studyChatRoom.name = name;
        return studyChatRoom;
    }
}
