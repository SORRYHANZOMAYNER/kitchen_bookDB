package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String image;
    @ManyToMany
    @JsonIgnore
    private List<Recipe> usedRecipes = new ArrayList<>();
    public void addRecipe(Category category) {
        List<Recipe> recipes = category.getUsedRecipes();
        for(Recipe r : recipes) {
            List<Category> categoryList = r.getCategories();
            categoryList.add(this);
        }
    }
    public void removeRecipe(Category category) {
        List<Recipe> recipes = category.getUsedRecipes();
        for(Recipe r : recipes) {
            List<Category> categoryList = r.getCategories();
            categoryList.remove(this);
        }
    }
}
