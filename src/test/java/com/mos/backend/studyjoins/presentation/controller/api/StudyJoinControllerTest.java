package com.mos.backend.studyjoins.presentation.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.jwt.TokenUtil;
import com.mos.backend.securityuser.WithMockCustomUser;
import com.mos.backend.studyjoins.application.StudyJoinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyJoinController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser(userId = 1L)
class StudyJoinControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudyJoinService studyJoinService;
    @MockitoBean
    private TokenUtil tokenUtil;

    @Test
    @DisplayName("스터디 신청 승인 성공 문서화")
    void approveStudyJoin_Success_Documentation() throws Exception {
        mockMvc.perform(
                        patch("/studies/1/study-joins/1/approval")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("study-joins-approve-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @DisplayName("스터디 신청 거절 성공 문서화")
    void rejectStudyJoin_Success_Documentation() throws Exception {
        mockMvc.perform(
                        patch("/studies/1/study-joins/1/rejection")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("study-joins-reject-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}
