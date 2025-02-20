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

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<String> createFeedback(@RequestBody Feedback feedback, @PathVariable long userId, @PathVariable long recipeId) {
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        Recipe recipe = recipeService.findById(recipeId);
        feedback.setCreatedDate(LocalDateTime.now());
        if(kitchenUser != null){
            kitchenUser.addFeedback(feedback);
        }
        if(recipe != null){
            recipe.addFeedback(feedback);
        }
        feedbackService.save(feedback);
        return ResponseEntity.ok("Ok"); //Переделать
    }
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.findAll();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.findById(id);
        return feedback != null
                ? ResponseEntity.ok(feedback)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/feedback/{id}/{recipeId}/{userId}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id,@PathVariable Long recipeId,@PathVariable Long userId) {
        Feedback feedback = feedbackService.findById(id);
        KitchenUser kitchenUser = kitchenUserService.findById(userId);
        Recipe recipe = recipeService.findById(recipeId);
        if(kitchenUser != null){
            kitchenUser.removeFeedback(feedback);
        }
        if(recipe != null){
            recipe.removeFeedback(feedback);
        }
        feedbackService.save(feedback);
        return ResponseEntity.ok("Ok"); //Переделать
    }
    @PutMapping("/feedback/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedbackDetails) {
        Feedback existingFeedback = feedbackService.findById(id);
        if (existingFeedback == null) {
            return ResponseEntity.notFound().build();
        }
        existingFeedback.setMark(feedbackDetails.getMark());
        existingFeedback.setContent(feedbackDetails.getContent());
        existingFeedback.setCreatedDate(feedbackDetails.getCreatedDate());
        Feedback updatedFeedback = feedbackService.save(existingFeedback);
        return ResponseEntity.ok(updatedFeedback);
    }

}
