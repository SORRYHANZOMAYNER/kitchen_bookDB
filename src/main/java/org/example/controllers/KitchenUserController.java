package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.models.KitchenUser;
import org.example.services.KitchenUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/api1/v1")
public class KitchenUserController {
    private final KitchenUserService userService;
    @Autowired
    public KitchenUserController(KitchenUserService userService){
        this.userService = userService;
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
}
