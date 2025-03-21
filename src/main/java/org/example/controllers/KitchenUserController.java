package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.configs.security.JwtService;
import org.example.dto.TokenDTO;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.KitchenUserService;
import org.example.services.MinioService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api1/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class KitchenUserController {
    private final KitchenUserService userService;
    private final RecipeService recipeService;
    private final MinioService minioService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    public KitchenUserController(KitchenUserService userService, RecipeService recipeService, MinioService minioService){
        this.userService = userService;
        this.recipeService = recipeService;
        this.minioService = minioService;
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> createMovieUser(@RequestBody KitchenUser user){
        System.out.println(user);
        if (user.getUsername().length() > 15) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Длина логина должна быть меньше 15 символов"));
        }
        KitchenUser userFromDB = userService.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Такой логин уже существует"));
        } else {
            userService.save(user);
            System.out.println("Пользователь зарегистрирован: " + user);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Успешная регистрация"));
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.authenticate(username, password));
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<TokenDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return ResponseEntity.ok(userService.refreshToken(request, response));
    }
    @GetMapping("/userme")
    public ResponseEntity<KitchenUser> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        KitchenUser user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    //@PreAuthorize("isFullyAuthenticated()")
//    @GetMapping("/user/me")
//    public KitchenUser getCurrentUser(@AuthenticationPrincipal KitchenUser user) {
//        return userService.getCurrentAuthenticatedUser();
//    }
    //@PreAuthorize("isFullyAuthenticated()")
    @PutMapping("/user/me")
    public void updateMovieUser(@RequestBody KitchenUser updatedUser,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        KitchenUser existingUser = userService.findByUsername(username);
        existingUser.setName(updatedUser.getName());
        existingUser.setSurName(updatedUser.getSurName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setTelephoneNumber(updatedUser.getTelephoneNumber());
        userService.updateUser(existingUser);
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
    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("/movieUser/avatar/me")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        KitchenUser movieUser = (KitchenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (movieUser == null) {
            return ResponseEntity.notFound().build();
        }
        String fileName = movieUser.getUsername() + "-" + UUID.randomUUID() + "." + getFileExtension(file.getOriginalFilename());
        try {
            String avatarLink = minioService.uploadFile(file.getInputStream(), fileName);
            movieUser.setUserImage(avatarLink);
            userService.updateUser(movieUser);

            return ResponseEntity.ok("Аватар обновлен успешно: " + avatarLink);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка в обновлении аватара: " + e.getMessage());
        }
    }
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
