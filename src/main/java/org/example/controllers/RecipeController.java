package org.example.controllers;

import org.example.models.Feedback;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api1/v1")
public class RecipeController {
    private final RecipeService recipeService;
    private final KitchenUserService kitchenUserService;
    @Autowired
    public RecipeController(RecipeService recipeService,KitchenUserService kitchenUserService) {
        this.recipeService = recipeService;
        this.kitchenUserService = kitchenUserService;
    }
    @PostMapping("/recipe/{userId}")
    public ResponseEntity<String> createRecipe(@RequestBody Recipe recipe,@PathVariable long userId) {
        if(recipe.getCategories()!=null && recipe.getCategories().size()>0) {
            recipe.addCategory(recipe);
        }
        if(recipe.getIngredients()!=null && recipe.getIngredients().size()>0) {
            recipe.addIngredient(recipe);
        }
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        if(kitchenUser!=null) {
            kitchenUser.addRecipe(recipe);
        }
        recipe.setCreatedDate(LocalDateTime.now());
        recipe.setLastModifiedDate(LocalDateTime.now());
        recipeService.save(recipe);
        return ResponseEntity.ok("Ok"); //Переделать
    }
    @GetMapping("/recipe")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.findAll();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);
        return recipe != null
                ? ResponseEntity.ok(recipe)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/recipe/{id}/{userId}")
    public void deleteRecipe(@PathVariable long id,@PathVariable long userId) {
        Recipe recipe = recipeService.findById(id);
        if(recipe.getCategories()!=null && recipe != null) {
            recipe.removeCategory(recipe);
        }
        if(recipe.getIngredients()!=null && recipe != null) {
            recipe.removeIngredient(recipe);
        }
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        if(kitchenUser!=null) {
            kitchenUser.removeRecipe(recipe);
        }
        recipeService.deleteById(id);
    }
    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @RequestBody Recipe recipeUpdate) {
        Recipe existingRecipe = recipeService.findById(id);
        existingRecipe.setTitle(recipeUpdate.getTitle());
        existingRecipe.setDescription(recipeUpdate.getDescription());
        existingRecipe.setImage(recipeUpdate.getImage());
        existingRecipe.setPlan(recipeUpdate.getPlan());
        existingRecipe.setCookingTime(recipeUpdate.getCookingTime());
        existingRecipe.setQuantityPortion(recipeUpdate.getQuantityPortion());
        existingRecipe.setCreatedDate(recipeUpdate.getCreatedDate());
        List<Feedback> existingFeedbacks = existingRecipe.getFeedbacks();
        List<Feedback> newFeedbacks = recipeUpdate.getFeedbacks();
        existingRecipe.setLastModifiedDate(LocalDateTime.now());
        if (newFeedbacks != null) {
            for (Feedback newFeedback : newFeedbacks) {
                if (!existingFeedbacks.contains(newFeedback)) {
                    existingRecipe.addFeedback(newFeedback);
                }
            }
            for (Feedback existingFeedback : existingFeedbacks) {
                if (!newFeedbacks.contains(existingFeedback)) {
                    existingRecipe.removeFeedback(existingFeedback);
                }
            }
        }
        Recipe savedRecipe = recipeService.save(existingRecipe);
        return ResponseEntity.ok(savedRecipe);
    }
}
