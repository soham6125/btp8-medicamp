package com.example.btp8.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class ErrorResponseDto {
    @NonNull private String message;
}
