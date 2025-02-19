package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Recipe", description = "The Recipe API")
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
    public ResponseEntity<String> createMovieUser(@RequestBody Recipe recipe,@PathVariable long userId) {
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
}
