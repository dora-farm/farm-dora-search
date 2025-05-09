package com.farmdora.farmdora.sale.controller;

import com.farmdora.farmdora.auth.PrincipalUtil;
import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.service.SellerSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_SALES_SUCCESS;

@RestController
@RequestMapping("${api.prefix}/my/admin/sale")
@RequiredArgsConstructor
public class AdminSaleController {
    private final SellerSaleService saleService;
    private final PrincipalUtil principalUtil;

    @PostMapping
    public ResponseEntity<?> searchWithJson(@RequestBody SaleSearchRequestDto searchCondition) {
        Pageable pageable = searchCondition.toPageable();
        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSalesAdmin(searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_SALES_SUCCESS.getMessage(), result));
    }

    @GetMapping
    public ResponseEntity<?> searchWithParams() {
        SaleSearchRequestDto searchCondition = SaleSearchRequestDto.builder().build();
        Pageable pageable = searchCondition.toPageable();
        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSalesAdmin(searchCondition, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_SALES_SUCCESS.getMessage(), result));
    }
}
