package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
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
    private String token;
    @NotNull
    private String password;
    @NotNull
    private String createdAt;
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Appointment> appointments;
}
