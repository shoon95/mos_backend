package com.mos.backend.userschedules.entity;

import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_schedules")
public class UserSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_schedules_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = true)
    private LocalDateTime endDateTime;

    public static UserSchedule create(User user, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        UserSchedule userSchedule = new UserSchedule();
        userSchedule.title = title;
        userSchedule.user = user;
        userSchedule.description = description;
        userSchedule.startDateTime = startDateTime;
        userSchedule.endDateTime = endDateTime;
        return userSchedule;
    }

    public void update(String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
