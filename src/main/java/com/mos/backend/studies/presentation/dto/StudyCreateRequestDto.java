package com.mos.backend.studies.presentation.dto;

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
  private List<CurriculumDto> curriculums;
  private List<String> rules;
  private List<String> benefits;
  private List<ApplicationQuestionDto> applicationQuestions;

  @Getter
  @Setter
  public static class CurriculumDto {
    private String title;
    private String content;
  }

  @Getter
  @Setter
  public static class ApplicationQuestionDto {
    private String question;
    private boolean isRequired;
    private String answerType;
    private List<String> options;
  }

}
