package com.mos.backend.studies.infrastructure.viewcount;

public interface ViewCountRepository {

    void saveViewRecord(Long studyId, String ipAddress);
    boolean existsByStudyIdAndIpAddress(Long studyId, String ipAddress);
}
