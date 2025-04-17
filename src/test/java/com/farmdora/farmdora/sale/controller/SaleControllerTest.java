package com.farmdora.farmdora.sale.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_RELATED_SALES_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.GET_SALE_DETAIL_SUCCESS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto.OptionDetailDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

public class SaleControllerTest extends ControllerTest {

    @Nested
    @DisplayName("상품 상세 조회 API 테스트")
    class GetSaleDetail {

        @Test
        @DisplayName("상품 상세 조회 API 성공")
        void testGetSaleDetailAPI() throws Exception {
            // given
            SaleDetailDto saleDetailDto = SaleDetailDto.builder()
                    .files(List.of("file1", "file2"))
                    .options(List.of(new OptionDetailDto(), new OptionDetailDto()))
                    .origin("국산")
                    .isLike(true)
                    .content("내용")
                    .build();
            when(saleService.getSaleDetail(anyInt(), anyInt())).thenReturn(saleDetailDto);

            // when
            // then
            mvc.perform(get("/sale/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(GET_SALE_DETAIL_SUCCESS.getMessage())))
                    .andExpect(jsonPath("$.data.files.size()", equalTo(2)))
                    .andExpect(jsonPath("$.data.origin", equalTo("국산")));
        }

        @Test
        @DisplayName("상품 상세 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetSaleDetailAPI_SaleNotFoundException() throws Exception {
            // given
            when(saleService.getSaleDetail(anyInt(), anyInt())).thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/sale/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }

    @Nested
    @DisplayName("관련 상품 목록 조회 API 테스트")
    class GetRelatedSales {
        @Test
        @DisplayName("관련 상품 목록 조회 API 성공")
        void testGetRelatedSalesAPI() throws Exception {
            // given
            List<SaleRelatedDto> relatedSales = List.of(
                    SaleRelatedDto.builder()
                            .saleId(1)
                            .isLike(true)
                            .reviewCount(10L)
                            .build(),
                    SaleRelatedDto.builder()
                            .saleId(1)
                            .isLike(true)
                            .reviewCount(10L)
                            .build()
            );
            when(saleService.getRelatedSales(anyInt(), anyInt(), any(Pageable.class))).thenReturn(relatedSales);

            // when
            // then
            mvc.perform(get("/sale/related/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(GET_RELATED_SALES_SUCCESS.getMessage())))
                    .andExpect(jsonPath("$.data.size()", equalTo(2)));
        }

        @Test
        @DisplayName("관련 상품 목록 조회시 상품이 존재하지 않을 경우 예외 발생 API 테스트")
        void testGetRelatedSalesAPI_SaleNotFoundException() throws Exception {
            // given
            when(saleService.getRelatedSales(anyInt(), anyInt(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Sale", 1));

            // when
            // then
            mvc.perform(get("/sale/related/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }
}
