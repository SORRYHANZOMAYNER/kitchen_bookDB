package org.example.controllers;

import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api1/v1")
public class KitchenUserController {
    private final KitchenUserService userService;
    private final RecipeService recipeService;
    @Autowired
    public KitchenUserController(KitchenUserService userService, RecipeService recipeService){
        this.userService = userService;
        this.recipeService = recipeService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> createMovieUser(@RequestBody KitchenUser user){
        if (user.getUsername().length() > 15) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Длина логина должна быть меньше 15 символов");
        }
        KitchenUser userFromDB = userService.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Такой логин уже существует");
        } else {
            userService.save(user);
            System.out.println("Пользователь зарегистрирован: " + user);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Успешная регистрация");
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody KitchenUser user) {
        boolean isAuthenticated = userService.authenticate(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Вход выполнен успешно");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }
    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/user/me")
    public KitchenUser getCurrentUser(@AuthenticationPrincipal KitchenUser user) {
        return userService.getCurrentAuthenticatedUser();
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
    @PutMapping("/user/{id}")
    public ResponseEntity<KitchenUser> updateUser(
            @PathVariable Long id,
            @RequestBody KitchenUser user) {
        KitchenUser existingUser = userService.findById(id);
        if(user.getUsername()!=null){
            existingUser.setUsername(user.getUsername());
        }
        if(user.getPassword()!=null){
            existingUser.setPassword(user.getPassword());
        }
        if(user.getEmail()!=null){
            existingUser.setEmail(user.getEmail());
        }
        if(user.getName()!=null){
            existingUser.setName(user.getName());
        }
        if(user.getSurName()!=null){
            existingUser.setSurName(user.getSurName());
        }
        userService.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }
}
