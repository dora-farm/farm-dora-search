package com.farmdora.farmdora.sale.service;

import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaleRedisService {

    private final SaleRepository saleRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper om;

    private static final int SEARCH_SIZE = 50;
    private static final int PAGE_SIZE = 10;
    private static final int POPULAR_SALES_DURATION = 30;
    private static final String POPULAR_SALES_KEY = "popular:sales:page:";
    private static final String POPULAR_SALES_COUNT_KEY = "popular:sales:count";

    @Scheduled(cron = "0 */10 * * * *")
    public void cachePopularSales() {
        log.info("인기 상품 목록을 업데이트합니다...");

        Pageable pageable = PageRequest.of(0, SEARCH_SIZE);
        Page<SaleRankingDto> sales = saleRepository.findTop50ByOrderCount(pageable);
        List<SaleRankingDto> content = sales.getContent();
        int total = content.size();
        redisTemplate.opsForValue().set(POPULAR_SALES_COUNT_KEY, total, Duration.ofMinutes(10));

        for (int page = 0; page < (total + PAGE_SIZE - 1) / PAGE_SIZE; page++) {
            int start = page * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, total);

            String key = POPULAR_SALES_KEY + page;
            List<SaleRankingDto> subList = content.subList(start, end);
            redisTemplate.opsForValue().set(key, subList, Duration.ofMinutes(POPULAR_SALES_DURATION));
        }

        log.info("인기 상품 목록 업데이트를 종료합니다...");
    }

    public List<SaleRankingDto> findSaleRanks(int page) {
        log.info("인기 상품 목록을 캐시에서 조회합니다.");

        Object cachedSalesValue = redisTemplate.opsForValue().get(POPULAR_SALES_KEY + page);
        if (cachedSalesValue == null) return null;

        List<?> cachedSales = (List<?>) cachedSalesValue;
        return cachedSales.stream()
                .map(obj -> om.convertValue(obj, SaleRankingDto.class))
                .collect(Collectors.toList());
    }

    public Integer findSaleRankCount() {
        return (Integer) redisTemplate.opsForValue().get(POPULAR_SALES_COUNT_KEY);
    }
}

