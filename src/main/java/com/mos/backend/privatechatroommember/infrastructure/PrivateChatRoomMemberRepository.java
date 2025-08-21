package com.mos.backend.privatechatroommember.infrastructure;

import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface PrivateChatRoomMemberRepository {
    boolean existsByPrivateChatRoomAndUser(PrivateChatRoom privateChatRoom, User user);

    PrivateChatRoomMember save(PrivateChatRoomMember privateChatRoomMember);

    Optional<PrivateChatRoomMember> findByUserAndPrivateChatRoom(User user, PrivateChatRoom privateChatRoom);

    Optional<PrivateChatRoomMember> findByUserIdAndPrivateChatRoomId(Long userId, Long privateChatRoomId);

    List<PrivateChatRoomMember> findByPrivateChatRoom(PrivateChatRoom privateChatRoom);
}
