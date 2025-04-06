package com.mos.backend.privatechatrooms.presentation.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mos.backend.common.jwt.TokenUtil;
import com.mos.backend.privatechatrooms.application.PrivateChatRoomService;
import com.mos.backend.privatechatrooms.presentation.req.PrivateChatRoomCreateReq;
import com.mos.backend.securityuser.WithMockCustomUser;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrivateChatRoomController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser(userId = 1L)
class PrivateChatRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PrivateChatRoomService privateChatRoomService;
    @MockitoBean
    private TokenUtil tokenUtil;

    @Test
    @DisplayName("개인 채팅방 생성 성공 문서화")
    void create_Private_Chat_Room_Success_Documentation() throws Exception {
        mockMvc.perform(
                        post("/private-chat-rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new PrivateChatRoomCreateReq(2L)))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-private-chat-room-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @DisplayName("개인 채팅방 조회 성공 문서화")
    void get_Private_Chat_Room_Id_Success_Documentation() throws Exception {
        mockMvc.perform(
                        get("/private-chat-rooms/search")
                                .param("counterpartId", String.valueOf(2L))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-private-chat-room-id-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}