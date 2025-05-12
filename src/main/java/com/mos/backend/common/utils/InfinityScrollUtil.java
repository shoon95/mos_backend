package com.mos.backend.common.utils;

import java.util.List;
import java.util.Optional;

public class InfinityScrollUtil {
    public static <T> Optional<T> getLastElement(List<T> content) {
        if (content.isEmpty()) return Optional.empty();

        T t = content.get(content.size() - 1);

        return Optional.ofNullable(t);
    }

    public static <T> boolean hasNext(List<T> list, int pageSize) {
        return list.size() > pageSize;
    }

}
