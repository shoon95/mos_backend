package com.mos.backend.privatechatroommember.infrastructure;

import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;

public interface PrivateChatRoomMemberRepository {
    boolean existsByPrivateChatRoomAndUser(PrivateChatRoom privateChatRoom, User user);

    PrivateChatRoomMember save(PrivateChatRoomMember privateChatRoomMember);
}
