package com.cra.portfolio.service;

import com.cra.portfolio.model.Category;
import com.cra.portfolio.model.Step;
import com.cra.portfolio.repository.CategoryRepository;
import com.cra.portfolio.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StepService {
    private final StepRepository stepRepository ;
    @Autowired
    private CategoryService categoryService ;


    public List<Step> getAllSteps(){
        return stepRepository.findAll();
    }
    public List<Category> getAllStepCategory(Integer StepId){
        Optional<Step> stepOptional =stepRepository.findById(StepId);
        Step step=stepOptional.get();
        return step.getCategories();
    }
}
