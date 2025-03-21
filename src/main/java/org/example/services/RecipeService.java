package org.example.services;

import org.example.models.Recipe;
import org.example.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class RecipeService{

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    public List<Recipe> getAllRecipes(String title, String search) {
        Specification<Recipe> specification = Specification.where(null);

        if (title != null) {
            specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), title));
        }
        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                    ));
        }
        return recipeRepository.findAll(specification);
    }
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }
    @Transactional
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
    @Transactional
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}