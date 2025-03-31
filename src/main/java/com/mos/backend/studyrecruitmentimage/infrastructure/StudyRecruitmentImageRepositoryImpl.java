package com.mos.backend.studyrecruitmentimage.infrastructure;

import com.mos.backend.studyrecruitmentimage.entity.StudyRecruitmentImage;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyRecruitmentImageRepositoryImpl implements StudyRecruitmentImageRepository{

    private final StudyRecruitmentImageJpaRepository studyRecruitmentImageJpaRepository;

    @Override
    public List<StudyRecruitmentImage> findAllByUser(User user) {
        return studyRecruitmentImageJpaRepository.findAllByUser(user);
    }

    @Override
    public void save(StudyRecruitmentImage studyRecruitmentImage) {
        studyRecruitmentImageJpaRepository.save(studyRecruitmentImage);
    }

    @Override
    public void delete(StudyRecruitmentImage studyRecruitmentImage) {
        studyRecruitmentImageJpaRepository.delete(studyRecruitmentImage);
    }
}
