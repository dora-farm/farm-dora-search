package com.farmdora.farmdora.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    SEARCH_ORDER_SUCCESS("주문 목록 조회에 성공하였습니다."),
    SEARCH_SALES_SUCCESS("상품 목록 조회에 성공하였습니다."),
    GET_SALE_DETAIL_SUCCESS("상품 상세 조회에 성공하였습니다."),
    SEARCH_QUESTION_SUCCESS("문의 목록 조회에 성공하였습니다."),
    SEARCH_REVIEWS_SUCCESS("리뷰 목록 조회에 성공하였습니다."),
    GET_RELATED_SALES_SUCCESS("관련 상품 목록 조회에 성공하였습니다."),
    GET_SALES_RANK("상품 랭킹 정보 조회에 성공하였습니다."),
    GET_SALES_BY_CATEGORIES("카테고리의 상품 목록 조회에 성공하였습니다."),
    SEARCH_USERS_SUCCESS("사용자 목록 조회에 성공하였습니다."),
    GET_CATEGORIES_SUCCESS("카테고리 목록 조회에 성공하였습니다.");

    private final String message;
}
