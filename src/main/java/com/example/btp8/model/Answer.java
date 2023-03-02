package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@Table(name = "answer")
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private long userID;
    @NotNull
    private Integer submissionID;
    @NotNull
    private String category;
    @NotNull
    private String option;
    @NotNull
    private Timestamp createdAt;
}

