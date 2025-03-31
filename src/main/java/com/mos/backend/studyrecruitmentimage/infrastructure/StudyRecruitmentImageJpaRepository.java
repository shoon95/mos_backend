package com.mos.backend.studyrecruitmentimage.infrastructure;

import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRecruitmentImageJpaRepository extends JpaRepository<StudyRecruitmentImage, Long> {
    List<StudyRecruitmentImage> findAllByUser(User user);
}
