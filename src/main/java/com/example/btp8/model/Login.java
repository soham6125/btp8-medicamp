package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Entity
@Table(name = "login")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private boolean isContact;
    @Null
    private String contact;
    @Null
    private String email;
    @NotNull
    private String password;
}
