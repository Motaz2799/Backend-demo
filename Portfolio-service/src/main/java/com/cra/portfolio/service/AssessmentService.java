package com.cra.portfolio.service;

import com.cra.portfolio.model.*;
import com.cra.portfolio.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentService {
    private final AssessmentRepository assessmentRepository ;
    @Autowired
    private QuestionService questionService ;

    public List<Assessment> getAllAssessments(){
        return assessmentRepository.findAll();
    }

    public void createAssessment(Assessment assessment){
        assessment.setCreatedAt(LocalDateTime.now());
        assessmentRepository.save(assessment);
    }

    public Assessment editQuestionInAssessment( Assessment assessment, List<AssessmentResponse> assessmentResponses){

            for(Step step : assessment.getSteps()){
                for (Category category :step.getCategories()){
                    for (Question question :category.getQuestions()){
                        if (assessmentResponses.stream().anyMatch(r -> r.getQuestion().equals(question))) {
                            AssessmentResponse response = assessmentResponses.stream().filter(r -> r.getQuestion().equals(question)).findFirst().orElse(null);
                            questionService.editQuestion(question.getId(), response.getResponse());
                        }
                        else {
                            questionService.editQuestion(question.getId(), null);

                        }
                    }
                }
            }
        return assessmentRepository.save(assessment);

    }







}
