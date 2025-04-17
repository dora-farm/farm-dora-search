package com.farmdora.farmdora.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    SEARCH_ORDER_SUCCESS("주문 목록 조회에 성공하였습니다."),
    SEARCH_SALES_SUCCESS("상품 목록 조회에 성공하였습니다."),
    GET_SALE_DETAIL_SUCCESS("상품 상세 조회에 성공하였습니다."),
    GET_RELATED_SALES_SUCCESS("관련 상품 목록 조회에 성공하였습니다.");

    private final String message;
}
