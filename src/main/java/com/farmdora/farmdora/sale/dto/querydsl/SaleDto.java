package com.farmdora.farmdora.sale.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class SaleDto {
    private int saleId;
    private String title;
    private boolean isBlind;
    private int price;
    private int stock;

    @QueryProjection
    public SaleDto(int saleId, String title, boolean isBlind, int price, int stock) {
        this.saleId = saleId;
        this.title = title;
        this.isBlind = isBlind;
        this.price = price;
        this.stock = stock;
    }
}
