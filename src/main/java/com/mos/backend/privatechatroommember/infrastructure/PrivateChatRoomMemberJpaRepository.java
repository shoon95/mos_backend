package com.mos.backend.privatechatroommember.infrastructure;

import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatRoomMemberJpaRepository extends JpaRepository<PrivateChatRoomMember, Long> {
    boolean existsByPrivateChatRoomAndUser(PrivateChatRoom privateChatRoom, User user);
}
