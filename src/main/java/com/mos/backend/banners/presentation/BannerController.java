package com.mos.backend.banners.presentation;

import com.mos.backend.banners.application.BannerService;
import com.mos.backend.banners.application.responsedto.BannerResponseDto;
import com.mos.backend.banners.presentation.requestdto.BannerCreateRequestDto;
import com.mos.backend.banners.presentation.requestdto.BannerUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    /**
     * 새로운 배너를 생성
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public BannerResponseDto createBanner(
            @RequestPart("bannerData") @Valid BannerCreateRequestDto requestDto,
            @RequestPart("imageFile") MultipartFile imageFile) {
        return bannerService.createBanner(requestDto, imageFile);
    }

    /**
     * 모든 배너 목록을 순서대로 조회
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BannerResponseDto> findAllBanners() {
        return bannerService.findAllBanners();
    }

    /**
     * 특정 배너의 상세 정보를 조회
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BannerResponseDto findBanner(@PathVariable Long id) {
        return bannerService.findBanner(id);
    }

    /**
     * 기존 배너의 정보를 수정
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BannerResponseDto updateBanner(
            @PathVariable Long id,
            @RequestPart("bannerData") @Valid BannerUpdateRequestDto requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

       return bannerService.updateBanner(id, requestDto, imageFile);
    }

    /**
     * 특정 배너를 삭제
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
    }
}
