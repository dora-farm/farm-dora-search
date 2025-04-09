package com.farmdora.farmdora.order.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_ORDER_SUCCESS;

import com.farmdora.farmdora.common.response.HttpResponse;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my/seller/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/search")
    public ResponseEntity<?> searchOrders(Integer userId,
                                          OrderSearchRequestDto searchCondition,
                                          @PageableDefault Pageable pageable) {
        // TODO 스프링 시큐리티 구현 후 userId 가져오기
        PageResponseDto<OrderSearchResponseDto> orders = orderService.searchOrders(userId, searchCondition, pageable);
        return ResponseEntity
                .ok()
                .body(new HttpResponse(HttpStatus.OK, SEARCH_ORDER_SUCCESS.getMessage(), orders));
    }
}
