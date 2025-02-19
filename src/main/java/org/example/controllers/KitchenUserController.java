package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/api1/v1")
public class KitchenUserController {
    private final KitchenUserService userService;
    private final RecipeService recipeService;
    @Autowired
    public KitchenUserController(KitchenUserService userService,RecipeService recipeService){
        this.userService = userService;
        this.recipeService = recipeService;
    }
    @PostMapping("/user")
    public ResponseEntity<String> createMovieUser(@RequestBody KitchenUser user){
        if(user.getPassword().length()>=4&&user.getPassword().length()<=12 && user.getUsername().length()>=4&&user.getUsername().length()<=12){
            userService.save(user);
            return ResponseEntity.ok("Ok");
        }else{
            return ResponseEntity.badRequest().body("Длина пароля или логина должна быть от 4 до 12 символов");
        }
    }
    @GetMapping("/user")
    public ResponseEntity<List<KitchenUser>> getAllUsers() {
        List<KitchenUser> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<KitchenUser> getUserById(@PathVariable Long id) {
        KitchenUser user = userService.findById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/user/{id}")
    public void deleteUserWithoutDeletingRecipes(@PathVariable Long id) {
        KitchenUser user = userService.findById(id);

        List<Recipe> recipes = user.getRecipes();
        for (Recipe recipe : recipes) {
            recipe.setUserKit(null);
            recipeService.save(recipe);
        }
        userService.deleteById(id);
    }

}
