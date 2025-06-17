package com.mos.backend.hotstudies.application.hotstudyeventhandler;

import com.mos.backend.hotstudies.entity.HotStudyEventType;
import com.mos.backend.hotstudies.infrastructure.HotStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteEventHandler implements EventHandler{

    private final HotStudyRepository hotStudyRepository;

    @Override
    public void handle(Long studyId, HotStudyEventType type) {
        hotStudyRepository.remove(studyId);
    }

    @Override
    public List<HotStudyEventType> supportList() {
        return new ArrayList<>(List.of(HotStudyEventType.DELETE));
    }
}
