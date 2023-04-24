package com.example.btp8.exceptions;

public class DoctorNotFoundException extends IllegalArgumentException {
    public DoctorNotFoundException(Long id) {
        super("Doctor with id: " + id + " not found");
    }
}
