package com.cra.portfolio.controller;

import com.cra.portfolio.service.AssessmentResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/responses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AssessmentResponseController {
    @Autowired
    public AssessmentResponseService assessmentResponseService ;

    @PostMapping("/{applicationId}/question/{questionId}")
    public void AddAssessmentToApplication(@PathVariable Integer applicationId , @PathVariable Integer questionId , @RequestBody String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<Map<String,Object>>(){});

        String responseValue = (String) jsonMap.get("response");
        assessmentResponseService.createAssessmentResponse(applicationId,questionId,responseValue);
    }
}
