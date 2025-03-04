package com.mos.backend.studies.presentation.dto;

import com.mos.backend.studycurriculum.presentation.requestdto.StudyCurriculumCreateRequestDto;
import com.mos.backend.studyquestions.presentation.requestdto.StudyQuestionCreateRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudyCreateRequestDto {

  private String title;
  private String category;
  private int maxParticipants;
  private LocalDate recruitmentStartDate;
  private LocalDate recruitmentEndDate;
  private List<String> tags;
  private String meetingType;
  private String schedule;
  private String content;
  private String notice;
  private String requirements;
  private List<StudyCurriculumCreateRequestDto> curriculums;
  private List<String> rules;
  private List<String> benefits;
  private List<StudyQuestionCreateRequestDto> applicationQuestions;

}
