package com.mos.backend.userschedules.application.res;

import com.mos.backend.userschedules.entity.UserSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserScheduleRes {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public static UserScheduleRes from(UserSchedule userSchedule) {
        return new UserScheduleRes(userSchedule.getId(), userSchedule.getTitle(), userSchedule.getDescription(), userSchedule.getStartDateTime(), userSchedule.getEndDateTime());
    }
}
