package com.farmdora.farmdora.sale.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdora.farmdora.entity.Like;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Order;
import com.farmdora.farmdora.entity.OrderOption;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.entity.SaleType;
import com.farmdora.farmdora.entity.SaleTypeBig;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.sale.dto.SaleSortType;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class CustomSaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("카테고리에 해당하는 상품 조회 QueryDsl 테스트")
    void testSearchSalesByCategories() {
        // given
        User user1 = new User();
        User user2 = new User();
        em.persist(user1);
        em.persist(user2);

        SaleTypeBig bigType = SaleTypeBig.builder()
                .id((short) 1)
                .build();
        em.persist(bigType);

        SaleType type = SaleType.builder()
                .id((short) 1)
                .saleTypeBig(bigType)
                .build();
        em.persist(type);

        for (int i = 1; i <= 10; i++) {
            Sale sale = Sale.builder()
                    .title("Sale" + i)
                    .type(type)
                    .build();
            em.persist(sale);

            if (i % 2 != 0) {
                Review review = Review.builder()
                        .sale(sale)
                        .build();
                em.persist(review);
            }

            SaleFile saleFile1 = SaleFile.builder()
                    .saveFile("saleFile1")
                    .sale(sale)
                    .isMain(false)
                    .build();
            SaleFile saleFile2 = SaleFile.builder()
                    .saveFile("saleFile2")
                    .sale(sale)
                    .isMain(true)
                    .build();
            em.persist(saleFile1);
            em.persist(saleFile2);

            Order order1 = new Order();
            Order order2 = new Order();
            em.persist(order1);
            em.persist(order2);

            Option option1 = Option.builder()
                    .sale(sale)
                    .price(1000 * i)
                    .build();
            Option option2 = Option.builder()
                    .sale(sale)
                    .price(2000 * i)
                    .build();
            em.persist(option1);
            em.persist(option2);

            OrderOption orderOption1 = OrderOption.builder()
                    .order(order1)
                    .option(option1)
                    .build();
            em.persist(orderOption1);

            // 짝수번이 주문수가 가장 많음
            if (i % 2 == 0) {
                OrderOption orderOption2 = OrderOption.builder()
                        .order(order2)
                        .option(option2)
                        .build();
                em.persist(orderOption2);
            }

            // 짝수번이 좋아요수가 가장 많음
            if (i % 2 == 0) {
                em.persist(Like.builder()
                        .sale(sale)
                        .user(user1)
                        .build());
            }
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<SaleSummaryDto> salesByType = saleRepository.searchSalesByCategories(user1.getUserId(), bigType.getId(), type.getId(), SaleSortType.PRICE_DESC, pageable);
        Page<SaleSummaryDto> salesByBigType = saleRepository.searchSalesByCategories(user1.getUserId(), bigType.getId(), null, SaleSortType.ORDER_DESC, pageable);
        Page<SaleSummaryDto> salesByNoType = saleRepository.searchSalesByCategories(user1.getUserId(),null, null, SaleSortType.REVIEW_DESC, pageable);
        Page<SaleSummaryDto> salesByLikeCount = saleRepository.searchSalesByCategories(user1.getUserId(),null, null, SaleSortType.RECOMMEND, pageable);

        // then
        assertThat(salesByType.getContent().size()).isEqualTo(10);
        assertThat(salesByBigType.getContent().size()).isEqualTo(10);
        assertThat(salesByNoType.getContent().size()).isEqualTo(10);
        assertThat(salesByLikeCount.getContent().size()).isEqualTo(10);
    }
}