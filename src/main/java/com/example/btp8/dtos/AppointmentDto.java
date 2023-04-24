package com.example.btp8.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AppointmentDto {
    Long doctorId;
    String timeSlot;
    String createdAt;
}
