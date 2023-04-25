package com.example.btp8.exceptions;

public class DoctorAlreadyExistsException extends IllegalArgumentException {
    public DoctorAlreadyExistsException(String s) {
        super("Doctor with this " + s + " already exists");
    }
}
