package com.mos.backend.studychatmessages.infrastructure;

import com.mos.backend.studychatmessages.entity.StudyChatMessage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mos.backend.studychatmessages.entity.QStudyChatMessage.studyChatMessage;

@Repository
@RequiredArgsConstructor
public class StudyChatMessageQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<StudyChatMessage> findAllByChatRoomIdForInfiniteScroll(Long studyChatRoomId, Long lastStudyChatMessageId, Integer size) {
        return queryFactory
                .selectFrom(studyChatMessage)
                .where(
                        eqStudyChatRoomId(studyChatRoomId),
                        ltStudyChatMessageId(lastStudyChatMessageId)
                )
                .orderBy(studyChatMessage.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private static BooleanExpression ltStudyChatMessageId(Long lastStudyChatMessageId) {
        return lastStudyChatMessageId == null ? null : studyChatMessage.id.lt(lastStudyChatMessageId);
    }

    private static BooleanExpression eqStudyChatRoomId(Long studyChatRoomId) {
        return studyChatMessage.studyChatRoom.id.eq(studyChatRoomId);
    }
}
