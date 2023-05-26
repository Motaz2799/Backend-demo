package com.cra.portfolio.repository;

import com.cra.portfolio.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment,Integer> {
}
