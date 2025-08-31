package com.mos.backend.banners.infrastructure;

import com.mos.backend.banners.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BannerJpaRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderBySortOrderAsc();

    // 생성 시 순서 조정을 위한 쿼리
    @Modifying
    @Query("UPDATE Banner b SET b.sortOrder = b.sortOrder + 1 WHERE b.sortOrder >= :sortOrder")
    void incrementSortOrderGreaterThanOrEqual(@Param("sortOrder") int sortOrder);

    @Modifying
    @Query("UPDATE Banner b SET b.sortOrder = b.sortOrder - 1 WHERE b.sortOrder > :sortOrder")
    void decrementSortOrderGreaterThan(@Param("sortOrder") int sortOrder);

    long count();
}
