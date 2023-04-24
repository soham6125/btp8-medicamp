package com.example.btp8.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class DoctorResponseDto {
    private Long id;
    private String name;
    private Integer age;
    private String contact;
    private String email;
    private String address;
    private LocalDate dob;
    private String organisation;
    private String designation;
    private String areaOfExpertise;
    private String experience;
    private List<AppointmentResponseDto> appointments;
}
