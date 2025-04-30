package com.farmdora.farmdora.sale.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_RELATED_SALES_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_SALES_BY_CATEGORIES;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_SALES_RANK;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_SALE_DETAIL_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_QUESTION_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_REVIEWS_SUCCESS;

import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.sale.dto.QuestionResponseDto;
import com.farmdora.farmdora.sale.dto.ReviewDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedDto;
import com.farmdora.farmdora.sale.dto.SaleSortType;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import com.farmdora.farmdora.sale.service.SaleService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping("/{saleId}")
    public ResponseEntity<?> getSaleDetail(Principal principal, @PathVariable("saleId") Integer saleId) {
        Integer userId = Integer.parseInt(principal.getName());
        SaleDetailDto saleDetail = saleService.getSaleDetail(userId, saleId);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_SALE_DETAIL_SUCCESS.getMessage(), saleDetail));
    }

    @GetMapping("/related/{saleId}")
    public ResponseEntity<?> getRelatedSales(Principal principal,
                                             @PathVariable("saleId") Integer saleId,
                                             @PageableDefault Pageable pageable) {
        Integer userId = Integer.parseInt(principal.getName());
        List<SaleRelatedDto> relatedSales = saleService.getRelatedSales(userId, saleId, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_RELATED_SALES_SUCCESS.getMessage(), relatedSales));
    }

    @GetMapping("/review/{saleId}")
    public ResponseEntity<?> getSaleReviews(@PathVariable("saleId") Integer saleId,
                                            @PageableDefault Pageable pageable) {
        PageResponseDto<ReviewDetailDto> reviews = saleService.getSaleReviews(saleId, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_REVIEWS_SUCCESS.getMessage(), reviews));
    }

    @GetMapping("/question/{saleId}")
    public ResponseEntity<?> getSaleQuestions(@PathVariable("saleId") Integer saleId,
                                              @PageableDefault Pageable pageable) {
        PageResponseDto<QuestionResponseDto> questions = saleService.getSaleQuestions(saleId, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_QUESTION_SUCCESS.getMessage(), questions));
    }

    @GetMapping("/rank")
    public ResponseEntity<?> getSaleRank(@PageableDefault Pageable pageable) {
        PageResponseDto<SaleRankingDto> sales = saleService.getTop50Sales(pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_SALES_RANK.getMessage(), sales));
    }

    @GetMapping("/type")
    public ResponseEntity<?> getSalesByType(Principal principal,
                                            @RequestParam(required = false) Short bigTypeId,
                                            @RequestParam(required = false) Short typeId,
                                            @RequestParam(required = false) SaleSortType sort,
                                            @PageableDefault(size = 20) Pageable pageable) {
        Integer userId = Integer.parseInt(principal.getName());
        PageResponseDto<SaleSummaryDto> sales = saleService.getSalesByCategory(userId, bigTypeId, typeId, sort, pageable);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, GET_SALES_BY_CATEGORIES.getMessage(), sales));
    }
}
