package com.cra.portfolio.repository;

import com.cra.portfolio.model.Contact;
import com.cra.portfolio.model.Database;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatabaseRepository extends JpaRepository<Database, Integer> {
    List<Database> findAllByDeletedAtNull(Pageable paging);
    List<Database> findAllByDeletedAtNull();

    List<Database> findAllByDeletedAtIsNotNull(Pageable paging);
}

