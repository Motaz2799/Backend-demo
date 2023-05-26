package com.cra.portfolio.controller;

import com.cra.portfolio.model.Step;
import com.cra.portfolio.service.AssessmentService;
import com.cra.portfolio.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/steps")
@CrossOrigin("*")
public class StepController {
    @Autowired
    private StepService stepService;

    @GetMapping
    public List<Step> getAllSteps(){
        return stepService.getAllSteps();
    }
}
