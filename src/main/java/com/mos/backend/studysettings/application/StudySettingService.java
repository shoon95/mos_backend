package com.mos.backend.studysettings.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studysettings.application.res.StudySettingRes;
import com.mos.backend.studysettings.entity.StudySettings;
import com.mos.backend.studysettings.entity.exception.StudySettingsErrorCode;
import com.mos.backend.studysettings.infrastructure.StudySettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudySettingService {
    private final StudySettingRepository studySettingRepository;
    private final EntityFacade entityFacade;

    /**
     * 스터디 설정 생성
     * @param studyId 생성할 스터디 설정의 스터디 id
     */
    @Transactional
    public void createStudySettings(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        StudySettings studySettings = StudySettings.create(study);
        studySettingRepository.save(studySettings);
    }

    /**
     * 스터디 아이디로 스터디 설정 조회해서 업데이트
     * @param studyId 조회할 스터디 아이디
     * @param lateThresholdMinutes 수정할 지간 기준 시간
     * @param absenceThresholdMinutes 수정할 결석 기준 시간
     * @return
     */
    @Transactional
    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    public StudySettingRes update(Long studyId, Integer lateThresholdMinutes, Integer absenceThresholdMinutes) {
        StudySettings studySettings = studySettingRepository.findByStudyId(studyId)
                .orElseThrow(() -> new MosException(StudySettingsErrorCode.NOT_FOUND));
        studySettings.update(lateThresholdMinutes, absenceThresholdMinutes);
        return StudySettingRes.of(studySettings, studyId);
    }

    /**
     * 스터디 아이디로 스터디 설정 단 건 조회
     * @param studyId 조회할 스터디 설정의 스터디 아이디
     * @return 스터디 설정의 공용 응답 dto
     */
    @Transactional
    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    public StudySettingRes read(Long studyId) {
        StudySettings studySettings = studySettingRepository.findByStudyId(studyId)
                .orElseThrow(() -> new MosException(StudySettingsErrorCode.NOT_FOUND));
        return StudySettingRes.of(studySettings, studyId);
    }
}
