package com.cra.portfolio.repository;

import com.cra.portfolio.model.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Integer> {
}

