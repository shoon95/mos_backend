package com.mos.backend.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
public class InfinityScrollRes<T> {
    @Getter
    private List<T> content;
    @Getter
    private Long lastElementId;
    private boolean hasNext;

    public static <T> InfinityScrollRes<T> of(List<T> content, Long lastElementId, boolean hasNext) {
        InfinityScrollRes<T> infinityScrollRes = new InfinityScrollRes<>();
        infinityScrollRes.content = content;
        infinityScrollRes.hasNext = hasNext;
        infinityScrollRes.lastElementId = lastElementId;
        return infinityScrollRes;
    }

    public boolean hasNext() {
        return hasNext;
    }
}
