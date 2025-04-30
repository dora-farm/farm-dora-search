package com.farmdora.farmdora.sale.repository.impl;

import static com.farmdora.farmdora.entity.QLike.like;
import static com.farmdora.farmdora.entity.QOption.option;
import static com.farmdora.farmdora.entity.QOrderOption.orderOption;
import static com.farmdora.farmdora.entity.QReview.review;
import static com.farmdora.farmdora.entity.QSale.sale;
import static com.farmdora.farmdora.entity.QSaleFile.saleFile;

import com.farmdora.farmdora.sale.dto.QSaleSummaryDto;
import com.farmdora.farmdora.sale.dto.SaleSortType;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import com.farmdora.farmdora.sale.repository.CustomSaleRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class CustomSaleRepositoryImpl implements CustomSaleRepository {

    private final JPAQueryFactory queryFactory;

    public CustomSaleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SaleSummaryDto> searchSalesByCategories(Integer userId, Short bigTypeId, Short typeId,
                                                        SaleSortType sortType, Pageable pageable) {
        List<SaleSummaryDto> sales = queryFactory
                .select(new QSaleSummaryDto(
                        sale.id,
                        sale.title,
                        option.price.min(),
                        JPAExpressions
                                .selectOne()
                                .from(like)
                                .where(like.sale.eq(sale)
                                        .and(like.user.userId.eq(userId)))
                                .exists(),
                        saleFile.saveFile.max()))
                .from(sale)
                .leftJoin(option).on(option.sale.eq(sale))
                .leftJoin(orderOption).on(orderOption.option.eq(option))
                .leftJoin(review).on(review.sale.eq(sale))
                .leftJoin(saleFile).on(saleFile.sale.eq(sale).and(saleFile.isMain.isTrue()))
                .leftJoin(like).on(like.sale.eq(sale))
                .where(categoryCondition(typeId, bigTypeId))
                .groupBy(sale.id, sale.title)
                .orderBy(getSaleOrderSpecifier(sortType), sale.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(sale.count())
                .from(sale)
                .where(categoryCondition(typeId, bigTypeId));

        return PageableExecutionUtils.getPage(sales, pageable, countQuery::fetchOne);
    }

    private BooleanExpression categoryCondition(Short typeId, Short bigTypeId) {
        boolean hasType = typeId != null;
        boolean hasBigType = bigTypeId != null;

        if (hasType) {
            return sale.type.id.eq(typeId);
        } else if (hasBigType) {
            return sale.type.saleTypeBig.id.eq(bigTypeId);
        }
        return null;
    }

    private OrderSpecifier<?> getSaleOrderSpecifier(SaleSortType sort) {
        return switch (sort) {
            case ORDER_DESC -> orderOption.id.count().desc();
            case ORDER_ASC -> orderOption.id.count().asc();
            case REVIEW_DESC -> review.id.count().desc();
            case PRICE_ASC -> option.price.min().asc();
            case PRICE_DESC -> option.price.min().desc();
            case RECOMMEND -> like.id.count().desc();
            default -> sale.id.desc();
        };
    }
}
