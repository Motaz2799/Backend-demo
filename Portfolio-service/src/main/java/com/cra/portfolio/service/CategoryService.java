package com.cra.portfolio.service;

import com.cra.portfolio.model.Category;
import com.cra.portfolio.model.Question;
import com.cra.portfolio.repository.CategoryRepository;
import com.cra.portfolio.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository ;
    private final QuestionRepository questionRepository ;
    @Autowired
    private QuestionService questionService ;

    public Category createCategory (Category category){
        category.setCreatedAt(LocalDateTime.now());
        for(Question question : category.getQuestions()){
            question.setCategory(category);
            System.out.println(question.getOptions());
            Question question1 = questionService.createQuestion(question);

        }
        return categoryRepository.save(category);
    }

    public List<Question> getAllCategoryQuestions(Integer CategoryId ){
        Optional<Category> category =categoryRepository.findById(CategoryId);
        Category category1=category.get();
        return category1.getQuestions();

    }

    public Optional<Category> getCategoryById(Integer CategoryId){
        return categoryRepository.findById(CategoryId);
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }


}
