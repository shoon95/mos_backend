package com.mos.backend.studies.application;

import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studies.infrastructure.viewcount.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final StudyRepository studyRepository;
    private final ViewCountRepository viewCountRepository;

    @Transactional
    public void handleViewCount(Long studyId, String ipAddress) {
        if (isCountable(studyId, ipAddress)) {
            saveViewRecord(studyId, ipAddress);
            increaseViewCount(studyId);
        }
    }

    public boolean isCountable(Long studyId, String ipAddress) {
        return !viewCountRepository.existsByStudyIdAndIpAddress(studyId, ipAddress);
    }

    private void saveViewRecord(Long studyId, String ipAddress) {
        viewCountRepository.saveViewRecord(studyId, ipAddress);
    }

    private void increaseViewCount(long studyId) {
        studyRepository.increaseViewCount(studyId);
    }
}
