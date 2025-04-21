package com.farmdora.farmdora.opinion.repository;

import com.farmdora.farmdora.config.AuditConfig;
import com.farmdora.farmdora.entity.Order;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.Seller;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.opinion.dto.OpinionSearchRequestDto;
import com.farmdora.farmdora.opinion.dto.ReviewResponseDto;
import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import(AuditConfig.class)
public class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("판매자의 리뷰 목록 검색 QueryDsl 테스트")
    void testSearchReviews() {
        // given
        Seller seller = new Seller();
        em.persist(seller);

        Sale sale = Sale.builder()
                .title("sale")
                .build();
        em.persist(sale);

        User user = User.builder()
                .name("user")
                .build();
        em.persist(user);

        Order order = Order.builder()
                .user(user)
                .build();
        em.persist(order);

        for (int i = 1; i <= 10; i++) {
            Review review = Review.builder()
                    .content("review" + i)
                    .score((byte) (i % 5))
                    .order(order)
                    .build();
            em.persist(review);
        }

        // when
        OpinionSearchRequestDto searchCondition = OpinionSearchRequestDto.builder()
                .sellerId(seller.getId())
                .searchType(SearchType.PRODUCT)
                .keyword("sale")
                .searchPeriod(SearchPeriod.WEEK)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseDto> result = reviewRepository.searchReviews(searchCondition, pageable);

        // then
        System.out.println(result.getContent());
    }
}
