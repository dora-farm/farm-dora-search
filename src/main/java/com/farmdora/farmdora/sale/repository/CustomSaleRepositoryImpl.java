package com.farmdora.farmdora.sale.repository;

import static com.farmdora.farmdora.entity.QOption.option;
import static com.farmdora.farmdora.entity.QOrderOption.orderOption;
import static com.farmdora.farmdora.entity.QSale.sale;
import static com.farmdora.farmdora.entity.QSaleType.saleType;
import static com.farmdora.farmdora.entity.QSaleTypeBig.saleTypeBig;

import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.farmdora.farmdora.sale.dto.querydsl.QSaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.QSaleOrderCountDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class CustomSaleRepositoryImpl implements CustomSaleRepository {

    private final JPAQueryFactory queryFactory;

    public CustomSaleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SaleDto> searchSales(Integer sellerId, SaleSearchRequestDto searchCondition, Pageable pageable) {
        List<SaleDto> sales = queryFactory
                .select(
                        new QSaleDto(sale.id, sale.title, sale.isBlind, option.price.min(), option.quantity.sum())
                )
                .from(sale)
                .join(option).on(option.sale.eq(sale))
                .join(saleType).on(sale.type.eq(saleType))
                .join(saleTypeBig).on(saleType.saleTypeBig.eq(saleTypeBig))
                .where(
                        sale.seller.id.eq(sellerId),
                        titleContains(searchCondition.getKeyword()),
                        isBlindEq(searchCondition.getFilters()),
                        isSmallTypeEq(searchCondition.getTypeId()),
                        isBigTypeEq(searchCondition.getTypeBigId())
                )
                .groupBy(sale.id, sale.title, sale.isBlind, sale.createdDate)
                .orderBy(
                        salesCreatedDateOrderBy(searchCondition.getSort()),
                        salesIdOrderBy(searchCondition.getSort())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = createCountQuery(sellerId, searchCondition);

        return PageableExecutionUtils.getPage(sales, pageable, countQuery::fetchOne);
    }

    @Override
    public List<SaleOrderCountDto> searchSaleOrderCount(List<Integer> saleIds) {
        return queryFactory
                .select(new QSaleOrderCountDto(sale.id, orderOption.id.count()))
                .from(orderOption)
                .join(option).on(option.id.eq(orderOption.option.id))
                .where(sale.id.in(saleIds))
                .groupBy(sale.id)
                .fetch();
    }

    private JPAQuery<Long> createCountQuery(Integer sellerId, SaleSearchRequestDto searchCondition) {
        return queryFactory
                .select(sale.countDistinct())
                .from(sale)
                .join(option).on(option.sale.eq(sale))
                .join(saleType).on(sale.type.eq(saleType))
                .join(saleTypeBig).on(saleType.saleTypeBig.eq(saleTypeBig))
                .where(
                        sale.seller.id.eq(sellerId),
                        titleContains(searchCondition.getKeyword()),
                        isBlindEq(searchCondition.getFilters()),
                        isSmallTypeEq(searchCondition.getTypeId()),
                        isBigTypeEq(searchCondition.getTypeBigId())
                );
    }

    private BooleanExpression titleContains(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return sale.title.contains(keyword);
        }
        return null;
    }

    private BooleanExpression isBlindEq(Set<SaleStatus> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }

        boolean hasInstock = filters.contains(SaleStatus.INSTOCK);
        boolean hasPreorder = filters.contains(SaleStatus.PREORDER);

        if (hasPreorder && hasInstock) {
            return null;
        } else if (hasPreorder) {
            return sale.isBlind.isTrue();
        } else if (hasInstock) {
            return sale.isBlind.isFalse();
        }
        return null;
    }

    private BooleanExpression isSmallTypeEq(Short typeId) {
        if (typeId != null) {
            return sale.type.id.eq(typeId);
        }
        return null;
    }

    private BooleanExpression isBigTypeEq(Short typeBigId) {
        if (typeBigId != null) {
            return sale.type.saleTypeBig.id.eq(typeBigId);
        }
        return null;
    }

    private OrderSpecifier<?> salesCreatedDateOrderBy(Sort sort) {
        if (sort.equals(Sort.OLDEST)) {
            return sale.createdDate.asc();
        } else {
            return sale.createdDate.desc();
        }
    }

    private OrderSpecifier<?> salesIdOrderBy(Sort sort) {
        if (sort.equals(Sort.OLDEST)) {
            return sale.id.asc();
        } else {
            return sale.id.desc();
        }
    }
}
