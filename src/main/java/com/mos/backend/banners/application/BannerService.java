package com.mos.backend.banners.application;

import com.mos.backend.banners.application.responsedto.BannerResponseDto;
import com.mos.backend.banners.entity.Banner;
import com.mos.backend.banners.entity.exception.BannerErrorCode;
import com.mos.backend.banners.infrastructure.BannerRepository;
import com.mos.backend.banners.presentation.requestdto.BannerCreateRequestDto;
import com.mos.backend.banners.presentation.requestdto.BannerUpdateRequestDto;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymaterials.application.UploadType;
import com.mos.backend.studymaterials.application.fileuploader.Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {
    private static final Long FOLDER_NAME = 1L;
    private final BannerRepository bannerRepository;
    private final Uploader uploader;

    /**
     * 배너 생성
     */
    @Transactional
    public BannerResponseDto createBanner(BannerCreateRequestDto requestDto, MultipartFile imageFile) {
        long totalBanners = bannerRepository.count();
        int requestedOrder = requestDto.getSortOrder();

        // 배너 순서 검증
        if (requestedOrder < 1 || requestedOrder > totalBanners + 1) {
            throw new MosException(BannerErrorCode.SORT_ORDER_NOT_PROPER);
        }

        // 새로운 배너의 순서 뒤쪽 배너는 모두 순서 + 1
        bannerRepository.incrementSortOrderGreaterThanOrEqual(requestedOrder);

        // 배너 이미지 업로드
        String uuidFileName = uploader.generateUUIDFileName(imageFile);
        String newFilePath = uploader.uploadFileSync(uuidFileName, FOLDER_NAME, UploadType.BANNER, imageFile);

        Banner banner = Banner.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(newFilePath)
                .linkUrl(requestDto.getLinkUrl())
                .sortOrder(requestedOrder)
                .build();

        Banner savedBanner = bannerRepository.save(banner);
        return new BannerResponseDto(savedBanner);
    }

    /**
     * Banner 수정
     */
    @Transactional
    public BannerResponseDto updateBanner(Long bannerId, BannerUpdateRequestDto requestDto, MultipartFile imageFile) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new MosException(BannerErrorCode.NOT_FOUND));

        int originalOrder = banner.getSortOrder();
        int newOrder = requestDto.getSortOrder();

        long totalBanners = bannerRepository.count();

        // 배너 순서 검증
        if (newOrder < 1 || newOrder > totalBanners) {
            throw new MosException(BannerErrorCode.SORT_ORDER_NOT_PROPER);
        }

        String imageUrl = banner.getImageUrl();

        // 배너의 수정할 이미지가 존재하면 기존 이미지 삭제하고 새로운 이미지 업로드
        if (imageFile != null && !imageFile.isEmpty()) {
            uploader.deleteFile(imageUrl);
            String generateUUIDFileName = uploader.generateUUIDFileName(imageFile);
            imageUrl = uploader.uploadFileSync(generateUUIDFileName, FOLDER_NAME, UploadType.BANNER, imageFile);
        }

        // 배너의 순서를 변경했다면
        if (originalOrder != newOrder) {
            // 기존 순서 뒤쪽은 순서를 -1
            bannerRepository.decrementSortOrderGreaterThan(originalOrder);
            // 새로운 순서부터 뒤 순서는 +1
            bannerRepository.incrementSortOrderGreaterThanOrEqual(newOrder);
        }

        banner.update(
                requestDto.getTitle(),
                requestDto.getContent(),
                imageUrl,
                requestDto.getLinkUrl(),
                newOrder
        );

        return new BannerResponseDto(banner);
    }

    /**
     * Banner 다 건 조회
     */
    public List<BannerResponseDto> findAllBanners() {
        return bannerRepository.findAllByOrderBySortOrderAsc().stream()
                .map(BannerResponseDto::new)
                .toList();
    }

    /**
     * 단 건 조회
     */
    public BannerResponseDto findBanner(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new MosException(BannerErrorCode.NOT_FOUND));
        return new BannerResponseDto(banner);
    }

    /**
     * 배너 삭제
     */
    @Transactional
    public void deleteBanner(Long bannerId) {
        Banner bannerToDelete = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new MosException(BannerErrorCode.NOT_FOUND));

        int deletedOrder = bannerToDelete.getSortOrder();

        // S3에 업로드된 이미지 파일 삭제
        uploader.deleteFile(bannerToDelete.getImageUrl());

        bannerRepository.delete(bannerToDelete);

        // 빈자리를 채우기 위해 뒷순서 배너들을 1씩 앞으로 당김
        bannerRepository.decrementSortOrderGreaterThan(deletedOrder);
    }
}
