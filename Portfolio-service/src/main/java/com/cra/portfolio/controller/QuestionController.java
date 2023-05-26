package com.cra.portfolio.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;


import com.cra.portfolio.model.OperatingSystem;
import com.cra.portfolio.model.Question;
import com.cra.portfolio.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/questions")
@CrossOrigin("*")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question newQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(newQuestion, HttpStatus.CREATED);
    }
    @PutMapping("/{questionId}")
    public String editResponse( @PathVariable Integer questionId , @RequestBody String response1) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response1, new TypeReference<Map<String,Object>>(){});

        String responseValue = (String) jsonMap.get("response");
        questionService.editQuestion(questionId,responseValue);
        return responseValue ;
    }
}
