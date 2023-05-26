package com.cra.portfolio.repository;

import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.AssessmentResponse;
import com.cra.portfolio.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentResponseRepository extends JpaRepository<AssessmentResponse,Integer> {

    AssessmentResponse findByAppIdAndAndQuestion(Integer AppId, Question question);
    List<AssessmentResponse> findByAppId(Integer AppId);
}
