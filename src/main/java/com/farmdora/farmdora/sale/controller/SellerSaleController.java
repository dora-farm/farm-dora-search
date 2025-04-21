package com.farmdora.farmdora.sale.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_SALES_SUCCESS;

import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.service.SaleService;
import com.farmdora.farmdora.sale.service.SellerSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/my/seller/sale")
@RequiredArgsConstructor
public class SellerSaleController {
    private final SellerSaleService saleService;

    @PostMapping("/search")
    public ResponseEntity<?> searchWithJson(@RequestBody SaleSearchRequestDto searchCondition) {
        // TODO 스프링 시큐리티 구현 후 userId 가져오기
        log.info("상품 목록 검색: {}", searchCondition);
        Pageable pageable = searchCondition.toPageable();
        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSales(searchCondition.getSellerId(), searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_SALES_SUCCESS.getMessage(), result));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchWithParams() {
        // TODO 스프링 시큐리티 구현 후 userId 가져오기
        SaleSearchRequestDto searchCondition = SaleSearchRequestDto.builder().build();
        Pageable pageable = searchCondition.toPageable();
        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSales(1, searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_SALES_SUCCESS.getMessage(), result));
    }
}
