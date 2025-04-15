package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Sale;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findAllBySale(Sale sale);
}
