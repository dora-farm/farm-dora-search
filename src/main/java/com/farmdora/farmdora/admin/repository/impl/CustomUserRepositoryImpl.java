package com.farmdora.farmdora.admin.repository.impl;

import static com.farmdora.farmdora.entity.QSeller.seller;
import static com.farmdora.farmdora.entity.QUser.user;

import com.farmdora.farmdora.admin.dto.QUserSearchResponseDto;
import com.farmdora.farmdora.admin.dto.UserSearchRequestDto;
import com.farmdora.farmdora.admin.dto.UserSearchResponseDto;
import com.farmdora.farmdora.admin.dto.UserType;
import com.farmdora.farmdora.admin.repository.CustomUserRepository;
import com.farmdora.farmdora.order.dto.Sort;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserSearchResponseDto> searchUsers(UserSearchRequestDto searchCondition, Pageable pageable) {
        List<UserSearchResponseDto> users = queryFactory
                .select(new QUserSearchResponseDto(
                        user.userId,
                        user.id,
                        user.name,
                        user.isBlind,
                        seller.id.isNotNull(),
                        user.createdDate
                ))
                .distinct()
                .from(user)
                .leftJoin(seller).on(seller.user.eq(user))
                .where(
                        nameContains(searchCondition.getKeyword()),
                        roleTypeCondition(searchCondition.getTypes())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(usersOrderBy(searchCondition.getSort()), user.userId.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(user.userId.countDistinct())
                .from(user)
                .leftJoin(seller).on(seller.user.eq(user))
                .where(
                        nameContains(searchCondition.getKeyword()),
                        roleTypeCondition(searchCondition.getTypes())
                );

        return PageableExecutionUtils.getPage(users, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameContains(String name) {
        if (StringUtils.hasText(name)) {
            return user.name.contains(name);
        }
        return null;
    }

    private BooleanExpression roleTypeCondition(List<UserType> types) {
        if (types != null && !types.isEmpty()) {
            boolean hasUser = types.contains(UserType.USER);
            boolean hasSeller = types.contains(UserType.SELLER);

            if (hasUser && hasSeller) {
                return null;
            } else if (hasUser) {
                return seller.id.isNull();
            } else if (hasSeller) {
                return seller.id.isNotNull();
            }
        }
        return null;
    }

    private OrderSpecifier<?> usersOrderBy(Sort sort) {
        if (sort != null) {
            return switch (sort) {
                case LATEST -> user.createdDate.desc();
                case OLDEST -> user.createdDate.asc();
                default -> null;
            };
        }
        return user.createdDate.desc();
    }
}
