package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "kitchenuser")
public class KitchenUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userKit",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = false
    )
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();
    public void addFeedback(Feedback feedback) {
        if (this.feedbacks == null) {
            this.feedbacks = new ArrayList<>();
        }
        //feedbacks.add(feedback);
        feedback.setKitchenAuthor(this);
    }
    public void removeFeedback(Feedback feedback) {
        if (this.feedbacks != null){
            feedbacks.remove(feedback);
            feedback.setKitchenAuthor(null);
        }
    }
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setUserKit(this);
    }
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setUserKit(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
