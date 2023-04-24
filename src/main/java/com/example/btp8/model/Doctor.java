package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Doctor {
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
    @NotNull
    private String designation;
    @NotNull
    private String areaOfExpertise;
    @NotNull
    private String experience;
    @NotNull
    private String token;
    @NotNull
    private String password;
    @NotNull
    private String createdAt;
    @NotNull
    private List<String> timeSlots;
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Appointment> appointments;
}
