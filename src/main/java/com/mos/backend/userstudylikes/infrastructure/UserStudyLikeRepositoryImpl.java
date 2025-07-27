package com.mos.backend.userstudylikes.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import com.mos.backend.userstudylikes.application.response.LikesResponseDto;
import com.mos.backend.userstudylikes.entity.UserStudyLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserStudyLikeRepositoryImpl implements UserStudyLikeRepository {

    private final UserStudyLikeJpaRepository userStudyLikeJpaRepository;
    private final UserStudyLikeQueryDslRepository userStudyLikeQueryDslRepository;

    @Override
    public boolean existsUserStudyLikeByUserAndStudy(User user, Study study) {
        return userStudyLikeJpaRepository.existsUserStudyLikeByUserAndStudy(user, study);
    }

    @Override
    public void save(UserStudyLike userStudyLike) {
        userStudyLikeJpaRepository.save(userStudyLike);
    }

    @Override
    public void deleteByUserAndStudy(User user, Study study) {
        userStudyLikeJpaRepository.deleteByUserAndStudy(user, study);
    }

    @Override
    public Long getLikedCountByStudyId(Long studyId) {
        return userStudyLikeQueryDslRepository.getLikedCountByStudyId(studyId);
    }

    @Override
    public boolean isLikedByMe(Long studyId, Long currentUserId) {
        return userStudyLikeQueryDslRepository.isLikedByMe(studyId, currentUserId);
    }

    @Override
    public List<LikesResponseDto> getLikes(List<Long> studyIds, Long currentUserId) {
        return userStudyLikeQueryDslRepository.getLikes(studyIds, currentUserId);
    }
}
