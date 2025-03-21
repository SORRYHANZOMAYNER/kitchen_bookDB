package org.example.controllers;

import org.example.configs.security.JwtService;
import org.example.dto.FeedbackDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api1/v1")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final KitchenUserService kitchenUserService;
    private final RecipeService recipeService;
    private final JwtService jwtService;
    @Autowired
    public FeedbackController(FeedbackService feedbackService, KitchenUserService kitchenUserService, RecipeService recipeService, JwtService jwtService) {
        this.feedbackService = feedbackService;
        this.kitchenUserService = kitchenUserService;
        this.recipeService = recipeService;
        this.jwtService = jwtService;
    }
    @PostMapping("/feedback/{recipeId}")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody Feedback feedback, @PathVariable long recipeId,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        KitchenUser kitchenUser = kitchenUserService.findByUsername(username);
        Recipe recipe = recipeService.findById(recipeId);
        feedback.setCreatedDate(LocalDateTime.now());
        if(kitchenUser != null){
            kitchenUser.addFeedback(feedback);
        }
        if(recipe != null){
            recipe.addFeedback(feedback);
        }
        feedbackService.save(feedback);
        return ResponseEntity.ok(convertToDTO(feedback));
    }
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.findAll();
        return ResponseEntity.ok(feedbacks);
    }
    @GetMapping("/feedback/recipe/{recipeId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByRecipeId(@PathVariable long recipeId) {
        List<Feedback> feedbacks = recipeService.findById(recipeId).getFeedbacks();
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream()
                .map(FeedbackController::convertToDTO)
                .toList();

        return ResponseEntity.ok(feedbackDTOs);
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.findById(id);
        FeedbackDTO feedbackDTO = null;
        if(feedback != null){
            feedbackDTO = convertToDTO(feedback);
        }
        return ResponseEntity.ok(feedbackDTO);
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
        feedbackService.deleteById(id);
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
    public static FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setId(feedback.getId());
        feedbackDTO.setContent(feedback.getContent());
        feedbackDTO.setCreatedDate(feedback.getCreatedDate());
        feedbackDTO.setMark(feedback.getMark());
        if (feedback.getKitchenAuthor() != null) {
            feedbackDTO.setUsername(feedback.getKitchenAuthor().getUsername());
        }

        return feedbackDTO;
    }
}
