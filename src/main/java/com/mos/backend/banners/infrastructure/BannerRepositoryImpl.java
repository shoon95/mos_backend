package com.mos.backend.banners.infrastructure;

import com.mos.backend.banners.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepository{
    private final BannerJpaRepository bannerJpaRepository;

    @Override
    public long count() {
        return bannerJpaRepository.count();
    }

    @Override
    public void incrementSortOrderGreaterThanOrEqual(int sortOrder) {
        bannerJpaRepository.incrementSortOrderGreaterThanOrEqual(sortOrder);
    }

    @Override
    public void decrementSortOrderGreaterThan(int sortOrder) {
        bannerJpaRepository.decrementSortOrderGreaterThan(sortOrder);
    }

    @Override
    public List<Banner> findAllByOrderBySortOrderAsc() {
        return bannerJpaRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public Banner save(Banner banner) {
        return bannerJpaRepository.save(banner);
    }

    @Override
    public Optional<Banner> findById(Long bannerId) {
        return bannerJpaRepository.findById(bannerId);
    }

    @Override
    public void delete(Banner banner) {
        bannerJpaRepository.delete(banner);
    }
}
