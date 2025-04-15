package com.farmdora.farmdora.sale.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdora.farmdora.entity.Like;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("existsByUserIdAndSaleId 쿼리 메서드 테스트")
    void testExistsByUserIdAndSaleId() {
        // given
        Like like1 = Like.builder()
                .userId(1)
                .saleId(1)
                .build();
        Like like2 = Like.builder()
                .userId(2)
                .saleId(1)
                .build();
        em.persist(like1);
        em.persist(like2);

        em.flush();
        em.clear();

        // when
        boolean like = likeRepository.existsByUserIdAndSaleId(1, 1);

        // then
        assertThat(like).isEqualTo(true);
    }
}