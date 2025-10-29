package com.mos.backend.userstudylikes.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import com.mos.backend.userstudylikes.application.response.LikesResponseDto;
import com.mos.backend.userstudylikes.entity.UserStudyLike;

import java.util.List;

public interface UserStudyLikeRepository {
    boolean existsUserStudyLikeByUserAndStudy(User user, Study study);

    void save(UserStudyLike userStudyLike);

    void deleteByUserAndStudy(User user, Study study);

    Long getLikedCountByStudyId(Long studyId);

    boolean isLikedByMe(Long studyId, Long currentUserId);

    List<LikesResponseDto> getLikes(List<Long> studyIds, Long currentUserId);
}