package com.mos.backend.userschedules.infrastructure;

import com.mos.backend.userschedules.entity.UserSchedule;

import java.util.List;
import java.util.Optional;

public interface UserScheduleRepository {
    Optional<UserSchedule> findById(Long userScheduleId);

    void delete(UserSchedule userSchedule);

    void save(UserSchedule userSchedule);

    List<UserSchedule> findByUserId(Long userId);
}
