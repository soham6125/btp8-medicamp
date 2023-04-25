package com.example.btp8.exceptions;

public class UserAlreadyExistsException extends IllegalArgumentException {
    public UserAlreadyExistsException(String s) {
        super("User with this " + s + " already exists");
    }
}
