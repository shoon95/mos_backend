package com.mos.backend.banners.infrastructure;

import com.mos.backend.banners.entity.Banner;

import java.util.List;
import java.util.Optional;

public interface BannerRepository {
    long count();

    void incrementSortOrderGreaterThanOrEqual(int sortOrder);

    void decrementSortOrderGreaterThan(int sortOrder);

    List<Banner> findAllByOrderBySortOrderAsc();

    Banner save(Banner banner);

    Optional<Banner> findById(Long bannerId);

    void delete(Banner banner);
}
