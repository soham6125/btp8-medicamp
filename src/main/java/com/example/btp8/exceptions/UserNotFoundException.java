package com.example.btp8.exceptions;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(Long id) {
        super("User with id: " + id + " not found");
    }
}
