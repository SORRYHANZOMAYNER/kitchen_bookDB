package org.example.services;

import org.example.models.Ingredient;
import org.example.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService{

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }
    @Transactional
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
    @Transactional
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }
}
