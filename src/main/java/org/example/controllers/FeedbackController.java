package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.models.Feedback;
import org.example.models.KitchenUser;
import org.example.models.Recipe;
import org.example.services.FeedbackService;
import org.example.services.KitchenUserService;
import org.example.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Feedback", description = "The Feedback API")
@RestController
@RequestMapping("/api1/v1")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final KitchenUserService kitchenUserService;
    private final RecipeService recipeService;
    @Autowired
    public FeedbackController(FeedbackService feedbackService,KitchenUserService kitchenUserService,RecipeService recipeService) {
        this.feedbackService = feedbackService;
        this.kitchenUserService = kitchenUserService;
        this.recipeService = recipeService;
    }
    @PostMapping("/feedback/{userId}/{recipeId}")
    public ResponseEntity<String> createMovieUser(@RequestBody Feedback feedback, @PathVariable long userId, @PathVariable long recipeId) {
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        Recipe recipe = recipeService.findById(recipeId);
        kitchenUser.addFeedback(feedback);
        recipe.addFeedback(feedback);
        feedbackService.save(feedback);
        return ResponseEntity.ok("Ok"); //Переделать
    }

}
