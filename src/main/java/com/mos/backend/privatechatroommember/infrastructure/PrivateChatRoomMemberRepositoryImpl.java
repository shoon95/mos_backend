package com.mos.backend.privatechatroommember.infrastructure;

import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PrivateChatRoomMemberRepositoryImpl implements PrivateChatRoomMemberRepository {
    private final PrivateChatRoomMemberJpaRepository privateChatRoomMemberJpaRepository;

    @Override
    public boolean existsByPrivateChatRoomAndUser(PrivateChatRoom privateChatRoom, User user) {
        return privateChatRoomMemberJpaRepository.existsByPrivateChatRoomAndUser(privateChatRoom, user);
    }

    @Override
    public PrivateChatRoomMember save(PrivateChatRoomMember privateChatRoomMember) {
        return privateChatRoomMemberJpaRepository.save(privateChatRoomMember);
    }
}
