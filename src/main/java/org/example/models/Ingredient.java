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
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String unit;
    private double price;
    @ManyToMany
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();
    public void addRecipe(Ingredient ingredient) {
        List<Recipe> recipes = ingredient.getRecipes();
        for(Recipe r : recipes) {
            List<Ingredient> ingredients = r.getIngredients();
            ingredients.add(this);
        }
    }
    public void removeRecipe(Ingredient ingredient) {
        List<Recipe> recipes = ingredient.getRecipes();
        for(Recipe r : recipes) {
            List<Ingredient> ingredients = r.getIngredients();
            ingredients.remove(this);
        }
    }
}
