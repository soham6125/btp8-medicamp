package com.example.btp8.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Entity(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    @NotNull
    @ManyToOne
    private User user;
    @NotNull
    @ManyToOne
    @NotNull
    private Doctor doctor;
    @NotNull
    private String timeSlot;
    @NotNull
    private String createdAt;
}
