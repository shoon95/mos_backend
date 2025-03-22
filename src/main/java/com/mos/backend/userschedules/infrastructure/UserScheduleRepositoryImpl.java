package com.mos.backend.userschedules.infrastructure;

import com.mos.backend.userschedules.entity.UserSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserScheduleRepositoryImpl implements UserScheduleRepository {

    private final UserScheduleJpaRepository userScheduleJpaRepository;

    @Override
    public void save(UserSchedule userSchedule) {
        userScheduleJpaRepository.save(userSchedule);
    }

    @Override
    public List<UserSchedule> findByUserId(Long userId) {
        return userScheduleJpaRepository.findByUserId(userId);
    }

    @Override
    public void delete(UserSchedule userSchedule) {
        userScheduleJpaRepository.delete(userSchedule);
    }

    @Override
    public Optional<UserSchedule> findById(Long userScheduleId) {
        return userScheduleJpaRepository.findById(userScheduleId);
    }
}
