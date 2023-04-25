package com.example.btp8.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String contact;
    private String email;
    private String organisation;
    private String designation;
    private String areaOfExpertise;
    private String experience;
    private List<String> timeSlots;
    private List<AppointmentResponseDto> appointments;
}
