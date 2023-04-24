package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "answer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userID;
    @NotNull
    private String category;
    @NotNull
    private Long score;
    @NotNull
    private Long maxScore;
    @NotNull
    private Long createdAt;

}

