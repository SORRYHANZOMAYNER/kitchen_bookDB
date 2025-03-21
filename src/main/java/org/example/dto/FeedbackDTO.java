package org.example.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.models.KitchenUser;
import org.example.models.Recipe;

import java.time.LocalDateTime;
@Getter
@Setter
public class FeedbackDTO {
    private long id;
    private String content;
    private LocalDateTime createdDate;
    private String username;
    private int mark = 0;
}
