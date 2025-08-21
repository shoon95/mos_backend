package com.mos.backend.privatechatmessages.infrastructure;

import com.mos.backend.privatechatmessages.entity.PrivateChatMessage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.mos.backend.privatechatmessages.entity.QPrivateChatMessage.privateChatMessage;

@RequiredArgsConstructor
@Repository
public class PrivateChatMessageQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<PrivateChatMessage> findByChatRoomIdForInfiniteScroll(Long privateChatRoomId,
                                                                      Long lastPrivateChatMessageId,
                                                                      int size,
                                                                      LocalDateTime deletedAt) {
        return queryFactory
                .selectFrom(privateChatMessage)
                .where(
                        eqPrivateChatRoomId(privateChatRoomId),
                        ltPrivateChatMessageId(lastPrivateChatMessageId),
                        gtPrivateChatMessageCreatedAt(deletedAt)
                )
                .orderBy(privateChatMessage.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private static BooleanExpression gtPrivateChatMessageCreatedAt(LocalDateTime deletedAt) {
        return Objects.isNull(deletedAt) ? null : privateChatMessage.createdAt.gt(deletedAt);
    }

    private static BooleanExpression eqPrivateChatRoomId(Long privateChatRoomId) {
        return privateChatMessage.privateChatRoom.id.eq(privateChatRoomId);
    }

    private BooleanExpression ltPrivateChatMessageId(Long lastPrivateChatMessageId) {
        return lastPrivateChatMessageId == null ? null : privateChatMessage.id.lt(lastPrivateChatMessageId);
    }
}
