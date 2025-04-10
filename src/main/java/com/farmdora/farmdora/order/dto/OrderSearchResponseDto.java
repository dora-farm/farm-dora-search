package com.farmdora.farmdora.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResponseDto {
    private Integer orderId;
    private List<ProductResponseDto> products;
    private LocalDateTime createdDate;
    private String buyerName;
    private Integer totalPrice;
    private String orderStatus;

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponseDto {
        private Integer saleId;
        private String saleTitle;
        private List<OptionResponseDto> options;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionResponseDto {
        private Integer optionId;
        private String name;
        private Integer count;
        private Integer price;
    }
}
