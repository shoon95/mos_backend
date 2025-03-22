package com.mos.backend.userschedules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.userschedules.application.res.UserScheduleRes;
import com.mos.backend.userschedules.entity.UserSchedule;
import com.mos.backend.userschedules.infrastructure.UserScheduleRepository;
import com.mos.backend.userschedules.presentation.req.UserScheduleCreateReq;
import com.mos.backend.userschedules.presentation.req.UserScheduleUpdateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserScheduleService {
    private final UserScheduleRepository userScheduleRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public void createUserSchedule(Long userId, UserScheduleCreateReq req) {
        User user = entityFacade.getUser(userId);

        UserSchedule userSchedule = UserSchedule.create(user, req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime());
        userScheduleRepository.save(userSchedule);
    }

    @Transactional(readOnly = true)
    public List<UserScheduleRes> getUserSchedules(Long userId) {
        User user = entityFacade.getUser(userId);

        List<UserSchedule> userSchedules = userScheduleRepository.findByUserId(user.getId());
        return userSchedules.stream().map(UserScheduleRes::from).toList();
    }

    @Transactional
    public void updateUserSchedule(Long userId, Long userScheduleId, UserScheduleUpdateReq req) {
        User user = entityFacade.getUser(userId);
        UserSchedule userSchedule = entityFacade.getUserSchedule(userScheduleId);

        validateOwner(user, userSchedule.getUser().getId());

        userSchedule.update(req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime());
    }

    @Transactional
    public void deleteUserSchedule(Long userId, Long userScheduleId) {
        User user = entityFacade.getUser(userId);
        UserSchedule userSchedule = entityFacade.getUserSchedule(userScheduleId);

        validateOwner(user, userSchedule.getUser().getId());

        userScheduleRepository.delete(userSchedule);
    }

    private void validateOwner(User user, Long userId) {
        if (!user.isOwner(userId))
            throw new MosException(UserErrorCode.USER_FORBIDDEN);
    }
}
