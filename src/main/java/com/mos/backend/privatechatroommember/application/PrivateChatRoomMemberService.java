package com.mos.backend.privatechatroommember.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMember;
import com.mos.backend.privatechatroommember.entity.PrivateChatRoomMemberErrorCode;
import com.mos.backend.privatechatroommember.infrastructure.PrivateChatRoomMemberRepository;
import com.mos.backend.privatechatrooms.entity.PrivateChatRoom;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PrivateChatRoomMemberService {
    private final EntityFacade entityFacade;
    private final PrivateChatRoomMemberRepository privateChatRoomMemberRepository;

    @Transactional
    public void createPrivateChatRoomMember(PrivateChatRoom privateChatRoom, User user) {
        if (privateChatRoomMemberRepository.existsByPrivateChatRoomAndUser(privateChatRoom, user)) {
            throw new MosException(PrivateChatRoomMemberErrorCode.CONFLICT);
        }

        privateChatRoomMemberRepository.save(PrivateChatRoomMember.of(privateChatRoom, user));
    }

    public PrivateChatRoomMember findByUserAndPrivateChatRoom(User user, PrivateChatRoom privateChatRoom) {
        return privateChatRoomMemberRepository.findByUserAndPrivateChatRoom(user, privateChatRoom)
                .orElseThrow(() -> new MosException(PrivateChatRoomMemberErrorCode.NOT_FOUND));
    }

    @Transactional
    public void updateLastEntryAt(Long userId, Long privateChatRoomId) {
        PrivateChatRoomMember privateChatRoomMember = entityFacade.getPrivateChatRoomMember(userId, privateChatRoomId);
        privateChatRoomMember.updateLastEntryAt();
    }


    @Transactional(readOnly = true)
    public List<PrivateChatRoomMember> findByPrivateChatRoom(PrivateChatRoom privateChatRoom) {
        return privateChatRoomMemberRepository.findByPrivateChatRoom(privateChatRoom);
    }

    @Transactional
    public void leavePrivateChatRoom(Long userId, Long privateChatRoomId) {
        PrivateChatRoomMember privateChatRoomMember = privateChatRoomMemberRepository.findByUserIdAndPrivateChatRoomId(userId, privateChatRoomId)
                .orElseThrow(() -> new MosException(PrivateChatRoomMemberErrorCode.NOT_FOUND));

        privateChatRoomMemberRepository.delete(privateChatRoomMember);
    }
}
