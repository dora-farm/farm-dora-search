package com.farmdora.farmdora.sale.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class SaleFileRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private SaleFileRepository saleFileRepository;

    @Test
    @DisplayName("findAllBySale 쿼리 메서드 테스트")
    void testFindAllBySale() {
        // given
        Sale sale = new Sale();
        em.persist(sale);

        SaleFile saleFile1 = SaleFile.builder()
                .sale(sale)
                .saveFile("file1")
                .build();
        SaleFile saleFile2 = SaleFile.builder()
                .sale(sale)
                .saveFile("file1")
                .build();
        em.persist(saleFile1);
        em.persist(saleFile2);

        em.flush();
        em.clear();

        // when
        List<SaleFile> saleFiles = saleFileRepository.findAllBySale(sale);

        // then
        assertThat(saleFiles.size()).isEqualTo(2);
    }
}