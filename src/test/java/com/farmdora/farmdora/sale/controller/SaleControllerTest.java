package com.farmdora.farmdora.sale.controller;

import static com.farmdora.farmdora.common.response.SuccessMessage.GET_SALE_DETAIL_SUCCESS;
import static com.farmdora.farmdora.common.response.SuccessMessage.SEARCH_SALES_SUCCESS;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farmdora.farmdora.ControllerTest;
import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto.OptionDetailDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

class SaleControllerTest extends ControllerTest {

    private SaleSearchRequestDto searchCondition;
    private PageResponseDto<SaleSearchResponseDto> result;

    @BeforeEach
    void setUp() {
        searchCondition  = SaleSearchRequestDto.builder()
                .keyword("상추")
                .sort(Sort.LATEST)
                .filters(Set.of(SaleStatus.INSTOCK))
                .typeBigId((short) 1)
                .typeId((short) 2)
                .build();

        List<SaleSearchResponseDto> saleSearchResponseDtos = List.of(
                SaleSearchResponseDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .orderCount(3L)
                        .build(),
                SaleSearchResponseDto.builder()
                        .saleId(2)
                        .title("상추2")
                        .price(20000)
                        .isBlind(false)
                        .stock(200)
                        .orderCount(2L)
                        .build()
        );
        Page<SaleSearchResponseDto> pages = new PageImpl<>(saleSearchResponseDtos);
        result = new PageResponseDto(pages.getContent(), pages);
    }

    @Nested
    @DisplayName("상품 목록 조회 API 테스트")
    class SearchSales {

        @Test
        @DisplayName("JSON으로 상품 목록 조회 API 테스트")
        void testSearchSalesByJsonAPI() throws Exception {
            // given
            when(saleService.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(post("/my/seller/sale/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(searchCondition)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_SALES_SUCCESS.getMessage())));
        }

        @Test
        @DisplayName("파라미터로 상품 목록 조회 API 테스트")
        void testSearchSalesByParamsAPI() throws Exception {
            // given
            when(saleService.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(result);

            // when
            // then
            mvc.perform(get("/my/seller/sale/search")
                            .param("keyword", "상추")
                            .param("sort", "LATEST")
                            .param("typeBigId", "1")
                            .param("typeId", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", equalTo(200)))
                    .andExpect(jsonPath("$.message", equalTo(SEARCH_SALES_SUCCESS.getMessage())));
        }
    }

    @Nested
    @DisplayName("상품 상세 조회 API 테스트")
    class GetSaleDetail {

        @Test
        @DisplayName("상품 상세 조회 API 테스트")
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
            mvc.perform(get("/my/seller/sale/{saleId}", 1)
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
            mvc.perform(get("/my/seller/sale/{saleId}", 1)
                            .param("userId", "1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", equalTo(400)))
                    .andExpect(jsonPath("$.message", equalTo("Sale 데이터가 존재하지 않습니다 : '1'")));
        }
    }


}