package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String cookingTime;
    private String image;
    private String quantityPortion;
    private String plan;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate;


    @ManyToMany(mappedBy = "recipes",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ingredient> ingredients = new ArrayList<>();
    @ManyToMany(mappedBy = "usedRecipes",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();
    @ManyToOne
    private KitchenUser userKit;

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setRecipe(this);
    }
    public void removeFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
        feedback.setRecipe(null);
    }
    public void addIngredient(Recipe recipe) {
        List<Ingredient> ingredients1 = recipe.getIngredients();
        for(Ingredient ingredient : ingredients1) {
            List<Recipe> recipes = ingredient.getRecipes();
            recipes.add(this);
        }
    }
    public void addCategory(Recipe recipe) {
        List<Category> categories1 = recipe.getCategories();
        for(Category category : categories1) {
            List<Recipe> recipes = category.getUsedRecipes();
            recipes.add(this);
        }
    }
}
