package com.mos.backend.userschedules.infrastructure;

import com.mos.backend.userschedules.entity.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScheduleJpaRepository extends JpaRepository<UserSchedule, Long> {
    List<UserSchedule> findByUserId(Long userId);
}
