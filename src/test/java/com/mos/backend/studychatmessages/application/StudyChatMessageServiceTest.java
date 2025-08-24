package com.mos.backend.studychatmessages.application;

import com.mos.backend.common.EntitySaver;
import com.mos.backend.common.dto.InfinityScrollRes;
import com.mos.backend.securityuser.WithMockCustomUser;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studychatmessages.application.res.StudyChatMessageRes;
import com.mos.backend.studychatrooms.entity.StudyChatRoom;
import com.mos.backend.testconfig.AbstractTestContainer;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@WithMockCustomUser(userId = 1L)
@SpringBootTest
class StudyChatMessageServiceTest extends AbstractTestContainer {

    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private StudyChatMessageService studyChatMessageService;

    @Nested
    @DisplayName("스터디 채팅 메시지 조회 성공 시나리오")
    class GetPrivateChatMessagesSuccessScenarios {

        @DirtiesContext
        @Test
        @DisplayName("스터디 채팅 메시지 조회 성공")
        void getStudyChatMessages_Success() {
            // Given
            final int size = 5;
            Long lastStudyChatMessageId = null;
            User user1 = entitySaver.saveUser();
            User user2 = entitySaver.saveUser();
            Study study = entitySaver.saveStudy();
            StudyChatRoom studyChatRoom = entitySaver.saveStudyChatRoom(study);
            entitySaver.saveStudyMember(user1, study);
            entitySaver.saveStudyMember(user2, study);
            saveStudyChatMessages(user1, studyChatRoom, size);
            saveStudyChatMessages(user2, studyChatRoom, size);

            // When
            InfinityScrollRes<StudyChatMessageRes> result1 = studyChatMessageService.getStudyChatMessages(
                    studyChatRoom.getId(), lastStudyChatMessageId, size
            );
            InfinityScrollRes<StudyChatMessageRes> result2 = studyChatMessageService.getStudyChatMessages(
                    studyChatRoom.getId(), result1.getLastElementId(), size
            );

            // Then
            List<Long> contentIds1 = result1.getContent().stream().map(StudyChatMessageRes::getStudyChatMessageId).toList();

            assertThat(contentIds1).containsExactly(10L, 9L, 8L, 7L, 6L);
            assertThat(result1.getLastElementId()).isEqualTo(6L);
            assertThat(result1.hasNext()).isTrue();

            List<Long> contentIds2 = result2.getContent().stream().map(StudyChatMessageRes::getStudyChatMessageId).toList();

            assertThat(contentIds2).containsExactly(5L, 4L, 3L, 2L, 1L);
            assertThat(result2.getLastElementId()).isEqualTo(1L);
            assertThat(result2.hasNext()).isFalse();
        }

        private void saveStudyChatMessages(User user, StudyChatRoom studyChatRoom, int size) {
            for (int i = 0; i < size; i++) {
                entitySaver.saveStudyChatMessage(user, studyChatRoom, String.valueOf(i));
            }
        }
    }
}
