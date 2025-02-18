package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "kitchenuser")
public class KitchenUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;
    private String surName;
    private String email;
    private String username;
    private String telephoneNumber;
    private String userImage;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "kitchenAuthor",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userKit",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();
    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setKitchenAuthor(this);
    }
    public void removeFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
        feedback.setKitchenAuthor(null);
    }
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setUserKit(this);
    }
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setUserKit(null);
    }
}
