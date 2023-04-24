package com.example.btp8.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class AppointmentDto {
    @NonNull Long doctorId;
    @NonNull String timeSlot;
    String createdAt;
}
