package com.mos.backend.privatechatroommember.infrastructure;

import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<PrivateChatRoomMember> findByUserAndPrivateChatRoom(User user, PrivateChatRoom privateChatRoom) {
        return privateChatRoomMemberJpaRepository.findByUserAndPrivateChatRoom(user, privateChatRoom);
    }

    @Override
    public Optional<PrivateChatRoomMember> findByUserIdAndPrivateChatRoomId(Long userId, Long privateChatRoomId) {
        return privateChatRoomMemberJpaRepository.findByUserIdAndPrivateChatRoomId(userId, privateChatRoomId);
    }

    @Override
    public List<PrivateChatRoomMember> findByPrivateChatRoom(PrivateChatRoom privateChatRoom) {
        return privateChatRoomMemberJpaRepository.findByPrivateChatRoom(privateChatRoom);
    }

    @Override
    public void delete(PrivateChatRoomMember privateChatRoomMember) {
        privateChatRoomMemberJpaRepository.delete(privateChatRoomMember);
    }
}
