package com.farmdora.farmdora.sale.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdora.farmdora.entity.Like;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleType;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("findTop10ByTypeAndIdNotOrderByIdDesc 쿼리메서드 테스트")
    void testFindTop10ByTypeAndIdNotOrderByIdDesc() {
        // given
        User user = new User();
        em.persist(user);

        SaleType type = SaleType.builder()
                .id((short) 1)
                .name("소분류1")
                .build();
        em.persist(type);

        Sale excludeSale = Sale.builder()
                .type(type)
                .build();
        em.persist(excludeSale);

        for (int i = 1; i <= 9; i++) {
            Sale sale = Sale.builder()
                    .title("title" + i)
                    .type(type)
                    .build();
            em.persist(sale);

            if (i % 2 == 0) {
                Review review1 = Review.builder()
                        .sale(sale)
                        .score((byte) 3)
                        .build();
                Review review2 = Review.builder()
                        .sale(sale)
                        .score((byte) 4)
                        .build();
                em.persist(review1);
                em.persist(review2);
            } else {
                Like like = Like.builder()
                        .saleId(sale.getId())
                        .userId(user.getUserId())
                        .build();
                em.persist(like);

                Option option1 = Option.builder()
                        .sale(sale)
                        .price(1000)
                        .build();
                Option option2 = Option.builder()
                        .sale(sale)
                        .price(2000)
                        .build();
                em.persist(option1);
                em.persist(option2);
            }
        }
        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<SaleRelatedInfoDto> sales = saleRepository.findTop10SalesWithReviewCountByTypeAndExcludedId(type, excludeSale.getId(), pageable);

        // then
        assertThat(sales.size()).isEqualTo(9);
    }
}