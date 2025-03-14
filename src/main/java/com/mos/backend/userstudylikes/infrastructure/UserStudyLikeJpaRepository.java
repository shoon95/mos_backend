package com.mos.backend.userstudylikes.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.users.entity.User;
import com.mos.backend.userstudylikes.entity.UserStudyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStudyLikeJpaRepository extends JpaRepository<UserStudyLike, Long> {
    boolean existsUserStudyLikeByUserAndStudy(User user, Study study);

    void deleteByUserAndStudy(User user, Study study);
}
