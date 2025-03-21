package org.example.services;

import org.example.models.Category;
import org.example.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
