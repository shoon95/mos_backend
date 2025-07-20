package com.mos.backend.notifications.entity;

import com.mos.backend.common.entity.BaseTimeEntity;
import com.mos.backend.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@Table(name = "user_fcm_toknes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFcmToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String token;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    private User user;

    public static UserFcmToken create(User user, String token) {
        UserFcmToken userFcmToken = new UserFcmToken();
        userFcmToken.token = token;
        userFcmToken.user = user;
        return userFcmToken;
    }
}
