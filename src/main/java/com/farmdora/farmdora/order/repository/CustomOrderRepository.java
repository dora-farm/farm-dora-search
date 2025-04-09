package com.farmdora.farmdora.order.repository;

import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.order.dto.querydsl.OrderDetailDto;
import com.farmdora.farmdora.order.dto.OrderSearchRequestDto;
import com.farmdora.farmdora.order.dto.querydsl.OrderDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOrderRepository {
    Page<OrderDto> searchOrders(Integer sellerId, OrderSearchRequestDto searchCondition, Pageable pageable);
    List<OrderDetailDto> findOrderDetailsByIds(List<Integer> ids, Sort sort);
}
