package com.mos.backend.hotstudies.application;

import com.mos.backend.hotstudies.infrastructure.HotStudyJoinRepository;
import com.mos.backend.hotstudies.infrastructure.HotStudyLikeRepository;
import com.mos.backend.hotstudies.infrastructure.HotStudyViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotStudyScoreCalculator {
    private final HotStudyViewRepository hotStudyViewRepository;
    private final HotStudyLikeRepository hotStudyLikeRepository;
    private final HotStudyJoinRepository hotStudyJoinRepository;

    private static final long STUDY_VIEW_COUNT_WEIGHT = 1;
    private static final long STUDY_LIKE_COUNT_WEIGHT = 3;
    private static final long STUDY_JOIN_COUNT_WEIGHT = 5;

    public long calculate(Long articleId) {
        Long studyViewCount = hotStudyViewRepository.read(articleId);
        Long studyLikeCount = hotStudyLikeRepository.read(articleId);
        Long studyJointCount = hotStudyJoinRepository.read(articleId);


        return studyViewCount * STUDY_VIEW_COUNT_WEIGHT
                + studyLikeCount * STUDY_LIKE_COUNT_WEIGHT
                + studyJointCount * STUDY_JOIN_COUNT_WEIGHT;
    }
}
