package com.farmdora.farmdora.sale.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.order.dto.Sort;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.SaleStatus;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import com.farmdora.farmdora.sale.mapper.SaleMapper;
import com.farmdora.farmdora.sale.repository.LikeRepository;
import com.farmdora.farmdora.sale.repository.OptionRepository;
import com.farmdora.farmdora.sale.repository.SaleFileRepository;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private SaleFileRepository saleFileRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleService saleService;

    @Test
    @DisplayName("판매자의 상품 목록 검색 및 조회 서비스 레이어 테스트")
    void testSearchSales() {
        // given
        List<SaleDto> saleDtos = List.of(
                SaleDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .build(),
                SaleDto.builder()
                        .saleId(2)
                        .title("상추2")
                        .price(20000)
                        .isBlind(false)
                        .stock(200)
                        .build()
        );
        Page<SaleDto> saleSearchResponsePages = new PageImpl<>(saleDtos);
        when(saleRepository.searchSales(anyInt(), any(SaleSearchRequestDto.class), any(Pageable.class))).thenReturn(saleSearchResponsePages);

        List<SaleOrderCountDto> orderCounts = List.of(
                SaleOrderCountDto.builder()
                        .saleId(1)
                        .orderCount(1L)
                        .build(),
                SaleOrderCountDto.builder()
                        .saleId(2)
                        .orderCount(1L)
                        .build()
        );
        when(saleRepository.searchSaleOrderCount(anyList())).thenReturn(orderCounts);

        List<SaleSearchResponseDto> saleSearchResponseDtos = List.of(
                SaleSearchResponseDto.builder()
                        .saleId(1)
                        .title("상추1")
                        .price(10000)
                        .isBlind(false)
                        .stock(100)
                        .orderCount(1L)
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
        when(saleMapper.mapToSaleSearchResponseDto(anyList(), anyList(), anyList())).thenReturn(saleSearchResponseDtos);

        // when
        Integer sellerId = 1;
        SaleSearchRequestDto searchCondition = SaleSearchRequestDto.builder()
                .keyword("상추")
                .sort(Sort.LATEST)
                .filters(Set.of(SaleStatus.INSTOCK))
                .typeId((short) 1)
                .typeBigId((short) 1)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        PageResponseDto<SaleSearchResponseDto> result = saleService.searchSales(sellerId, searchCondition, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세 정보 조회 서비스 레이어 테스트")
    void testGetSaleDetail() {
        // given
        Sale mockSale = Sale.builder()
                .id(1)
                .title("상추")
                .content("유기농 상추")
                .origin("국산")
                .build();
        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(mockSale));

        List<Option> options = List.of(
                Option.builder()
                        .id(1)
                        .sale(mockSale)
                        .name("상추 옵션1")
                        .price(1000)
                        .build(),
                Option.builder()
                        .id(2)
                        .sale(mockSale)
                        .name("상추 옵션2")
                        .price(2000)
                        .build()
        );
        when(optionRepository.findAllBySale(any(Sale.class))).thenReturn(options);

        List<SaleFile> saleFiles = List.of(
                SaleFile.builder()
                        .sale(mockSale)
                        .saveFile("URL1")
                        .build(),
                SaleFile.builder()
                        .sale(mockSale)
                        .saveFile("URL2")
                        .build()
        );
        when(saleFileRepository.findAllBySale(any(Sale.class))).thenReturn(saleFiles);

        when(likeRepository.existsByUserIdAndSaleId(anyInt(), anyInt())).thenReturn(true);

        // when
        SaleDetailDto saleDetail = saleService.getSaleDetail(1, 1);

        // then
        assertThat(saleDetail.getContent()).isEqualTo("유기농 상추");
        assertThat(saleDetail.getOptions().size()).isEqualTo(2);
        assertThat(saleDetail.getFiles().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상세정보를 조회하고자 하는 상품이 존재하지 않을 경우 예외 발생테스트")
    void testGetSaleDetail_SaleNotFoundException() {
        // given
        when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> saleService.getSaleDetail(1, 1))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}