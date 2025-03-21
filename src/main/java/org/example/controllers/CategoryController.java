package org.example.controllers;

import org.example.models.Category;
import org.example.models.Feedback;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api1/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping("/category")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        if(category.getUsedRecipes()!=null){
            category.addRecipe(category);
        }
        categoryService.save(category);
        return ResponseEntity.ok("Ok");
    }
    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return category != null
                ? ResponseEntity.ok(category)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if(category.getUsedRecipes() != null){
            category.removeRecipe(category);
        }
        categoryService.deleteById(id);
        return ResponseEntity.ok("Ok");
    }
}
