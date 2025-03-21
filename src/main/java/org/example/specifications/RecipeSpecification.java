package org.example.specifications;

import org.example.models.Recipe;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RecipeSpecification {
    public static Specification<Recipe> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }
    public static Specification<Recipe> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("description"), "%" + description + "%");
        };
    }
    public static Specification<Recipe> filterByIngredients(List<String> name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("ingredients").get("name").in(name);
        };
    }
    public static Specification<Recipe> filterByCategory(List<String> name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("categories").get("name").in(name);
        };
    }

}
