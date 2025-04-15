package com.farmdora.farmdora.sale.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class SaleOrderCountDto {
    private Integer saleId;
    private Long orderCount;

    @QueryProjection
    public SaleOrderCountDto(Integer saleId, Long orderCount) {
        this.saleId = saleId;
        this.orderCount = orderCount;
    }
}
