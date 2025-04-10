package com.farmdora.farmdora.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.OrderSearchResponseDto;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import com.farmdora.farmdora.order.mapper.OrderMapper;
import com.farmdora.farmdora.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    @DisplayName("판매자의 주문 목록 조회 서비스 레이어 테스트")
    void testSearchOrders() {
        // given
        List<OrderDto> orders = new ArrayList<>(List.of(
                OrderDto.builder()
                        .orderId(1)
                        .buyerName("이다훈")
                        .orderStatus("판매중")
                        .createdDate(LocalDateTime.now())
                        .build(),
                OrderDto.builder()
                        .orderId(2)
                        .buyerName("이다훈")
                        .orderStatus("배송중")
                        .createdDate(LocalDateTime.now())
                        .build()
        ));
        PageImpl<OrderDto> pages = new PageImpl<>(orders);
        when(orderRepository.searchOrders(anyInt(), any(OrderSearchRequestDto.class), any(Pageable.class)))
                .thenReturn(pages);

        List<OrderDetailDto> orderDetails = new ArrayList<>(List.of(
                OrderDetailDto.builder()
                        .orderId(1)
                        .saleId(1)
                        .saleTitle("상품1")
                        .optionId(1)
                        .optionName("옵션1")
                        .quantity(100)
                        .price(1000)
                        .build(),
                OrderDetailDto.builder()
                        .orderId(1)
                        .saleId(2)
                        .saleTitle("상품2")
                        .optionId(2)
                        .optionName("옵션2")
                        .quantity(50)
                        .price(2000)
                        .build()
        ));
        when(orderRepository.findOrderDetailsByIds(anyList(), any())).thenReturn(orderDetails);

        List<OrderSearchResponseDto> orderSearchResponseDtos = new ArrayList<>(List.of(
                new OrderSearchResponseDto(),
                new OrderSearchResponseDto()
        ));
        PageResponseDto<OrderSearchResponseDto> pageResponseDto = new PageResponseDto<>(orderSearchResponseDtos, pages);
        when(orderMapper.toDto(any(Page.class), anyList())).thenReturn(pageResponseDto);

        // when
        PageResponseDto<OrderSearchResponseDto> result = orderService
                .searchOrders(1, mock(OrderSearchRequestDto.class), mock(Pageable.class));

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }
}