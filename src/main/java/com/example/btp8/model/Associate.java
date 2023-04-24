package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "associate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Associate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private Integer age;
    @NotNull
    private String contact;
    @NotNull
    private String email;
    @NotNull
    private String address;
    @NotNull
    private LocalDate dob;
    @NotNull
    private String organisation;
    private String areaOfExpertise;
    @NotNull
    private String token;
    @NotNull
    private String password;
    @NotNull
    private String createdAt;
}

