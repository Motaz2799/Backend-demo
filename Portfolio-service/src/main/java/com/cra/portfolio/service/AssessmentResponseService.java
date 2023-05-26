package com.cra.portfolio.service;

import com.cra.portfolio.model.Application;
import com.cra.portfolio.model.Assessment;
import com.cra.portfolio.model.AssessmentResponse;
import com.cra.portfolio.model.Question;
import com.cra.portfolio.repository.ApplicationRepository;
import com.cra.portfolio.repository.AssessmentResponseRepository;
import com.cra.portfolio.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class AssessmentResponseService {

    private final ApplicationRepository applicationRepository;
    private final QuestionRepository questionRepository ;
    @Autowired
    private AssessmentResponseRepository assessmentResponseRepository;

    @Autowired
    public QuestionService questionService;


    public void createAssessmentResponse(Integer appId, Integer questionId, String response) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));
        AssessmentResponse existingResponse = assessmentResponseRepository.findByAppIdAndAndQuestion(appId, question);

        if (existingResponse == null) {
            AssessmentResponse newResponse = new AssessmentResponse();
            newResponse.setQuestion(question);
            newResponse.setResponse(response);
            newResponse.setCreatedAt(LocalDateTime.now());
            newResponse.setModifiedAt(LocalDateTime.now());
            newResponse.setAppId(appId);
            assessmentResponseRepository.save(newResponse);
            questionService.editQuestion(questionId,response);

        } else {
            existingResponse.setResponse(response);
            existingResponse.setModifiedAt(LocalDateTime.now());
            assessmentResponseRepository.save(existingResponse);
            questionService.editQuestion(questionId,response);

        }
        Optional<Application> application = applicationRepository.findById(appId);
        Application application1= application.get();       }



    public List<AssessmentResponse> getAllAssessmentResponses() {
        return assessmentResponseRepository.findAll();
    }

    public AssessmentResponse getAssessmentResponseById(Integer id) {
        return assessmentResponseRepository.findById(id).orElse(null);
    }

    public AssessmentResponse getAssessmentResponseByApplicationAndQuestion(Application application, Question question) {
        return assessmentResponseRepository.findByAppIdAndAndQuestion(application.getId(), question);
    }

    public void deleteAssessmentResponse(Integer id) {
        AssessmentResponse assessmentResponse = assessmentResponseRepository.findById(id).orElse(null);
        if (assessmentResponse != null) {
            assessmentResponse.setDeletedAt(LocalDateTime.now());
            assessmentResponseRepository.save(assessmentResponse);
        }
    }
}
