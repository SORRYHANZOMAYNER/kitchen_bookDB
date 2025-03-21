package org.example.controllers;

import org.example.models.Category;
import org.example.models.Ingredient;
import org.example.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api1/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class IngredientController {
    private final IngredientService ingredientService;
    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }
    @PostMapping("/ingredient")
    public ResponseEntity<String> createIngredient(@RequestBody Ingredient ingredient) {
        if(ingredient.getRecipes()!=null){
            ingredient.addRecipe(ingredient);
        }
        ingredientService.save(ingredient);
        return ResponseEntity.ok("Ok");
    }
    @GetMapping("/ingredient")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.findAll();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        Ingredient ingredient = ingredientService.findById(id);
        return ingredient != null
                ? ResponseEntity.ok(ingredient)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long id) {
        Ingredient ingredient = ingredientService.findById(id);
        if(ingredient.getRecipes() != null){
            ingredient.removeRecipe(ingredient);
        }
        ingredientService.deleteById(id);
        return ResponseEntity.ok("Ok");
    }
}
