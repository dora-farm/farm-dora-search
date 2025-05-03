package com.farmdora.farmdora.admin.repository.impl;

import static com.farmdora.farmdora.entity.QPopup.popup;
import static com.farmdora.farmdora.entity.QPopupType.popupType;

import com.farmdora.farmdora.admin.dto.PopupSearchRequestDto;
import com.farmdora.farmdora.admin.repository.CustomPopupRepository;
import com.farmdora.farmdora.entity.Popup;
import com.farmdora.farmdora.order.dto.Sort;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class CustomPopupRepositoryImpl implements CustomPopupRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPopupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Popup> searchPopups(PopupSearchRequestDto searchCondition, Pageable pageable) {
        List<Popup> popups = queryFactory
                .select(popup)
                .from(popup)
                .join(popup.type, popupType).fetchJoin()
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate()),
                        popupTypeEq(searchCondition.getTypes())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(popupOrderBy(searchCondition.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(popup.count())
                .from(popup)
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        dateBetween(searchCondition.getStartDate(), searchCondition.getEndDate()),
                        popupTypeEq(searchCondition.getTypes())
                );

        return PageableExecutionUtils.getPage(popups, pageable, countQuery::fetchOne);
    }

    private BooleanExpression keywordContains(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return popup.title.contains(keyword);
        }
        return null;
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return popup.startDate.after(startDate.atStartOfDay()).and(popup.endDate.before(endDate.atStartOfDay()));
        } else if (startDate != null) {
            return popup.startDate.after(startDate.atStartOfDay());
        } else if (endDate != null){
            return popup.endDate.before(endDate.atStartOfDay());
        } else {
            return null;
        }
    }

    private BooleanExpression popupTypeEq(List<Short> types) {
        if (types != null && !types.isEmpty()) {
            return popup.type.id.in(types);
        }
        return null;
    }

    private OrderSpecifier<?> popupOrderBy(Sort sort) {
        if (sort == null || sort.equals(Sort.LATEST)) {
            return popup.id.desc();
        } else {
            return popup.id.asc();
        }
    }
}
