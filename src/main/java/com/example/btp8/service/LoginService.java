package com.example.btp8.service;

import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.repository.UserRepository;
import com.example.btp8.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginService {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User verifyUserLogin(Login loginBody) throws Exception {
        String contact = loginBody.getContact();
        String password = loginBody.getPassword();
        if (contact != null) {
            Optional<User> currentUser = userRepository.findUserByContact(contact);
            if (currentUser.isEmpty()) {
                throw new Exception("Invalid credentials");
            }
            String createdAtTimestamp = currentUser.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentUser.get().getPassword())) {
                return currentUser.get();
            } else {
                throw new Exception("Invalid credentials");
            }
        } else {
            String email = loginBody.getEmail();
            Optional<User> currentUser = userRepository.findUserByEmail(email);
            if (currentUser.isEmpty()) {
                throw new Exception("Invalid credentials");
            }
            String createdAtTimestamp = currentUser.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentUser.get().getPassword())) {
                return currentUser.get();
            } else {
                throw new Exception("Invalid credentials");
            }
        }
    }

}
