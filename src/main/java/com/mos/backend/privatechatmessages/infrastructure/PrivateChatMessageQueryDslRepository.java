package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mos.backend.privatechatmessages.entity.QPrivateChatMessage.privateChatMessage;

@RequiredArgsConstructor
@Repository
public class PrivateChatMessageQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<PrivateChatMessage> findByChatRoomIdForInfiniteScroll(Long privateChatRoomId, Long lastPrivateChatMessageId, int size) {
        return queryFactory
                .selectFrom(privateChatMessage)
                .where(
                        eqPrivateChatRoomId(privateChatRoomId),
                        ltPrivateChatMessageId(lastPrivateChatMessageId)
                )
                .orderBy(privateChatMessage.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private static BooleanExpression eqPrivateChatRoomId(Long privateChatRoomId) {
        return privateChatMessage.privateChatRoom.id.eq(privateChatRoomId);
    }

    private BooleanExpression ltPrivateChatMessageId(Long lastPrivateChatMessageId) {
        return lastPrivateChatMessageId == null ? null : privateChatMessage.id.lt(lastPrivateChatMessageId);
    }
}
