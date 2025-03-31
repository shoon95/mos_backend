package com.mos.backend.studyrecruitmentimage.infrastructure;

import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.users.entity.User;

import java.util.List;

public interface StudyRecruitmentImageRepository {
    List<StudyRecruitmentImage> findAllByUser(User user);

    void save(StudyRecruitmentImage studyRecruitmentImage);

    void delete(StudyRecruitmentImage studyRecruitmentImage);
}
