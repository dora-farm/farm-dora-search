package com.farmdora.farmdora.sale.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Order;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.entity.SaleType;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import com.farmdora.farmdora.sale.dto.ReviewDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import com.farmdora.farmdora.sale.repository.LikeRepository;
import com.farmdora.farmdora.sale.repository.OptionRepository;
import com.farmdora.farmdora.sale.repository.SaleFileRepository;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private ReviewRepository reviewRepository;

    @InjectMocks
    private SaleService saleService;

    @Nested
    @DisplayName("상품 상세 정보 조회 서비스 레이어 테스트")
    class GetSaleDetailsTests {

        @Test
        @DisplayName("상품 상세 정보 조회 성공")
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

    @Nested
    @DisplayName("관련 상품 목록 조회 서비스 레이어 테스트")
    class GetRelatedSales {

        @Test
        @DisplayName("관련 상품 목록 조회 성공")
        void testGetRelatedSales() {
            // given
            Sale mockSale = Sale.builder()
                    .id(1)
                    .type(new SaleType())
                    .build();
            when(saleRepository.findById(anyInt())).thenReturn(Optional.of(mockSale));

            List<SaleRelatedInfoDto> relatedSaleDetails = List.of(
                    SaleRelatedInfoDto.builder()
                            .saleId(2)
                            .title("sale2")
                            .price(10000)
                            .reviewCount(10L)
                            .score(3.5)
                            .build(),
                    SaleRelatedInfoDto.builder()
                            .saleId(3)
                            .title("sale3")
                            .price(20000)
                            .reviewCount(12L)
                            .score(4.0)
                            .build()
            );
            when(saleRepository.findTop10SalesWithReviewCountByTypeAndExcludedId(any(SaleType.class), anyInt(), any(Pageable.class))).thenReturn(relatedSaleDetails);

            when(likeRepository.existsByUserIdAndSaleId(anyInt(), anyInt())).thenReturn(true);

            SaleFile mockSaleFile = SaleFile.builder()
                    .sale(mockSale)
                    .isMain(true)
                    .originFile("origin_file")
                    .saveFile("save_file")
                    .build();
            when(saleFileRepository.findBySaleIdAndIsMainIsTrue(anyInt())).thenReturn(Optional.of(mockSaleFile));

            // when
            Pageable pageable = PageRequest.of(0, 10);
            List<SaleRelatedDto> relatedSales = saleService.getRelatedSales(1, 1, pageable);

            // then
            assertThat(relatedSales.size()).isEqualTo(2);
            assertThat(relatedSales.get(0).getSaleId()).isEqualTo(2);
            assertThat(relatedSales.get(1).getSaleId()).isEqualTo(3);
        }

        @Test
        @DisplayName("관련상품을 조회할 상품이 존재하지 않을 경우 예외 발생")
        void testGetRelatedSales_ResourceNotFoundException() {
            // given
            when(saleRepository.findById(anyInt())).thenReturn(Optional.empty());

            // when
            // then
            Pageable pageable = PageRequest.of(0, 10);
            assertThatThrownBy(() -> saleService.getRelatedSales(1, 1, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Test
    @DisplayName("상품의 리뷰 목록 조회 서비스 레이어 테스트")
    void testGetSaleReviews() {
        // given
        Order order = Order.builder()
                .user(User.builder().name("user").build())
                .build();

        List<Review> reviews = List.of(
            Review.builder()
                    .order(order)
                    .content("review1")
                    .score((byte) 2)
                    .build(),
            Review.builder()
                    .order(order)
                    .content("review2")
                    .score((byte) 3)
                    .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Review> reviewPages = new PageImpl<>(reviews, pageable, 2);
        when(reviewRepository.findAllBySaleId(anyInt(), any(Pageable.class))).thenReturn(reviewPages);

        // when
        PageResponseDto<ReviewDetailDto> result = saleService.getSaleReviews(1, pageable);

        // then
        assertThat(result.getContents().size()).isEqualTo(2);
    }
}