package com.cra.portfolio.repository;


import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Server;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Integer> {
    List<Server> findByServerName(String serverName);

    List<Server> findAllByDeletedAtNull(Pageable paging);

    List<Server> findAllByDeletedAtIsNull();

    List<Server> findAllByDeletedAtIsNotNull(Pageable paging);

}
