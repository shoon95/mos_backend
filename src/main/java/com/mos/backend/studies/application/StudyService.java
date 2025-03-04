package com.mos.backend.studies.application;

import com.mos.backend.studies.entity.Category;
import com.mos.backend.studies.entity.MeetingType;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.StudyTag;
import com.mos.backend.studies.presentation.dto.StudyCreateRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    /**
     * todo
     * 1. study create
     * 2. study - rules 생성
     * 3. study - benefits 생성
     * 4. study - questions 생성
     * 4. study - curriculum 생성
     * @param requestDto
     */
    @Transactional
    public void create(StudyCreateRequestDto requestDto) {
        Study study = Study.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .notice(requestDto.getNotice())
                .maxParticipantsCount(requestDto.getMaxParticipants())
                .category(Category.fromDescription(requestDto.getCategory()))
                .schedule(requestDto.getSchedule())
                .recruitmentStartDate(requestDto.getRecruitmentStartDate())
                .recruitmentEndDate(requestDto.getRecruitmentEndDate())
                .tags(StudyTag.fromList(requestDto.getTags()))
                .color(RandomColorGenerator.generateRandomColor())
                .meetingType(MeetingType.fromDescription(requestDto.getMeetingType()))
                .requirements(requestDto.getRequirements())
                .build();


    }
}
