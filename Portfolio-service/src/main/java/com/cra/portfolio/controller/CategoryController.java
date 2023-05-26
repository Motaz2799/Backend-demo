package com.cra.portfolio.controller;

import com.cra.portfolio.model.Category;
import com.cra.portfolio.model.Question;
import com.cra.portfolio.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
    @GetMapping("/{CategoryId}/questions")
    public List<Question> GetAllCategoryQuestions(@PathVariable Integer CategoryId) {
        return categoryService.getAllCategoryQuestions(CategoryId);

    }
    @GetMapping("/{CategoryId}")
    public Optional<Category> GetCategoryById(@PathVariable Integer CategoryId) {
        return categoryService.getCategoryById(CategoryId);

    }
    @GetMapping()
    public List<Category> GetAllCategories() {
        return categoryService.getAllCategories();

    }
}
