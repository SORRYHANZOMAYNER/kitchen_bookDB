package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        recipe.addCategory(recipe);
        recipe.addIngredient(recipe);
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        kitchenUser.addRecipe(recipe);
        recipeService.save(recipe);
        return ResponseEntity.ok("Ok"); //Переделать
    }
}
