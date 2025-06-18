package com.mos.backend.studychatrooms.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.redis.RedisStudyChatRoomUtil;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.studychatrooms.infrastructure.StudyChatRoomRepository;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyChatRoomService {
    private final RedisStudyChatRoomUtil redisStudyChatRoomUtil;
    private final EntityFacade entityFacade;
    private final StudyChatRoomRepository studyChatRoomRepository;

    @Transactional
    public void create(Long studyId) {
        Study study = entityFacade.getStudy(studyId);

        StudyChatRoom studyChatRoom = StudyChatRoom.create(study);

        studyChatRoomRepository.save(studyChatRoom);
    }

    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    @Transactional(readOnly = true)
    public void enter(Long userId, Long studyId, Long studyChatRoomId) {
        User user = entityFacade.getUser(userId);
        StudyChatRoom studyChatRoom = entityFacade.getStudyChatRoom(studyChatRoomId);
        redisStudyChatRoomUtil.enterChatRoom(user.getId(), studyChatRoom.getId());
    }

    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    @Transactional(readOnly = true)
    public void leave(Long userId, Long studyId, Long studyChatRoomId) {
        User user = entityFacade.getUser(userId);
        StudyChatRoom studyChatRoom = entityFacade.getStudyChatRoom(studyChatRoomId);
        redisStudyChatRoomUtil.leaveChatRoom(user.getId(), studyChatRoom.getId());
    }
}
