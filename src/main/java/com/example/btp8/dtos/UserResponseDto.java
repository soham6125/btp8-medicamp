package com.example.btp8.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private Integer age;
    private String contact;
    private String email;
    private String address;
    private LocalDate dob;
    private List<AppointmentResponseDto> appointments;
}
