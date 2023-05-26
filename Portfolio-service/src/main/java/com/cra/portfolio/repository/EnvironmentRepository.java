package com.cra.portfolio.repository;

import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnvironmentRepository extends JpaRepository<Environment, Integer> {
    List<Environment> findAllByDeletedAtNull(Pageable paging);
    List<Environment> findAllByDeletedAtIsNull();


}
