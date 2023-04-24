package com.example.btp8.dtos;

import lombok.Data;

@Data
public class AppointmentResponseDto {
    private Long id;
    private Long userId;
    private Long doctorId;
    private String timeSlot;
}
