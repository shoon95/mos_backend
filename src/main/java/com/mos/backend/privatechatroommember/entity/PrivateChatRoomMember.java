package com.mos.backend.privatechatroommember.entity;

import com.mos.backend.common.entity.BaseAuditableEntity;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE private_chat_room_members SET deleted_at = NOW() WHERE private_chat_room_member_id = ?")
@Table(name = "private_chat_room_members")
public class PrivateChatRoomMember extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "private_chat_room_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "private_chat_room_id")
    private PrivateChatRoom privateChatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime lastEntryAt = LocalDateTime.now();

    @ColumnDefault("NULL")
    private LocalDateTime deletedAt;

    public static PrivateChatRoomMember of(PrivateChatRoom privateChatRoom, User user) {
        PrivateChatRoomMember privateChatRoomMember = new PrivateChatRoomMember();
        privateChatRoomMember.privateChatRoom = privateChatRoom;
        privateChatRoomMember.user = user;
        privateChatRoomMember.lastEntryAt = LocalDateTime.now();
        return privateChatRoomMember;
    }

    public void updateLastEntryAt() {
        this.lastEntryAt = LocalDateTime.now();
    }
}
