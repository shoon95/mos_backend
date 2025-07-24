package com.mos.backend.studychatrooms.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.studychatrooms.infrastructure.StudyChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyChatRoomService {
    private final EntityFacade entityFacade;
    private final StudyChatRoomRepository studyChatRoomRepository;

    @Transactional
    public void create(Long studyId) {
        Study study = entityFacade.getStudy(studyId);

        StudyChatRoom studyChatRoom = StudyChatRoom.create(study);

        studyChatRoomRepository.save(studyChatRoom);
    }
}
