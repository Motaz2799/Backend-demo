package com.cra.portfolio.repository;

import com.cra.portfolio.model.DataCenter;
import com.cra.portfolio.model.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataCenterRepository extends JpaRepository<DataCenter, Integer> {
    List<DataCenter> findAllByDeletedAtNull(Pageable paging);

    List<DataCenter> findAllByDeletedAtIsNull();
}
