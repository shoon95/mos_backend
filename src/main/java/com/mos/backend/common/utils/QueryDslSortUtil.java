package com.mos.backend.common.utils;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslSortUtil {
    public static List<OrderSpecifier<?>> toOrderSpecifiers(Sort sort, Class<?> entityClass, String variable) {

        PathBuilder<?> pathBuilder = new PathBuilder<>(entityClass, variable);
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            String property = order.getProperty();  // 정렬할 프로퍼티명
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            // Comparable(숫자, 문자열, 날짜 등) 기준 정렬인 경우 사용
            Expression<?> path = pathBuilder.get(property);
            // 캐스팅 또는 getComparable() 등을 이용해 OrderSpecifier 생성
            orders.add(new OrderSpecifier<>(direction, (Expression) path));
        }
        return orders;
    }
}
