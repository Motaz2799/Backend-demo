package com.cra.portfolio.controller;

import com.cra.portfolio.model.Assessment;
import com.cra.portfolio.model.Category;
import com.cra.portfolio.service.AssessmentService;
import com.cra.portfolio.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assessments")
@CrossOrigin("*")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping()
    public List<Assessment> getAllAssessment(){
        return assessmentService.getAllAssessments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAssessment(@RequestBody Assessment assessment) {
         assessmentService.createAssessment(assessment);
    }
}
