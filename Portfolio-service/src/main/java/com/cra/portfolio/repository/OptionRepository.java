package com.cra.portfolio.repository;

import com.cra.portfolio.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option,Integer> {
}
