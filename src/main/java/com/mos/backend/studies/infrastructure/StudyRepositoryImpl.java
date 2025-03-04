package com.mos.backend.studies.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepository{

    private final StudyJpaRepository studyJpaRepository;


}
