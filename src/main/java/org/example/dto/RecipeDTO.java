package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecipeDTO {
    private long id;
    private String title;
    private String description;
    private String cookingTime;
    private String image;
    private String quantityPortion;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<PairList> instruction = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private String name;
    private String surname;

}