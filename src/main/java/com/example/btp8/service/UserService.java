package com.example.btp8.service;

import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.repository.UserRepository;
import com.example.btp8.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.*;

import static java.time.Period.between;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> healthCheck() {
        Map<String, String> res = new HashMap<>();
        res.put("message", "Welcome to MediCamp");
        return res;
    }

    public User findUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User with given ID does not exist");
        }
        return user.get();
    }

    public Page<User> findAllUsers(int page, int size, String email){
        return userRepository.userFilter(email, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    public User addUser(@Valid User user) throws Exception {

        String email = user.getEmail();
        Optional<User> checkUser = userRepository.findUserByEmail(email);
        if (checkUser.isPresent()){
            throw new Exception("User with this email ID already exists");
        }

        String contact = user.getContact();
        Optional<User> checkUserContact = userRepository.findUserByContact(contact);
        if (checkUserContact.isPresent()){
            throw new Exception("User with this contact number already exists");
        }

        UUID randomUUID = UUID.randomUUID();
        String tokenID = randomUUID.toString().replaceAll("-", "");
        user.setToken(tokenID);

        int age = between(user.getDob(), LocalDate.now()).getYears();
        user.setAge(age);

        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
//        System.out.println(currentTimestamp);
        String hashPassword = utils.get_SHA_512_SecurePassword(user.getPassword(), currentTimestamp);
//        System.out.println("hash => " + hashPassword);
        user.setPassword(hashPassword);
        user.setCreatedAt(currentTimestamp);
        return userRepository.save(user);
    }

    public User deleteUser(Long id) throws Exception {

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new Exception("User with given ID does not exist");
        }

        User currentUser = user.get();
        userRepository.delete(currentUser);
        return currentUser;
    }

    public User editUser(Long id, @Valid User user) throws Exception {

        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User with id " + id + " does not exist");
        }

        String email = user.getEmail();
        Optional<User> checkUser = userRepository.findUserByEmail(email);
        if (checkUser.isPresent()){
            throw new Exception("User with this email ID already exists");
        }

        String contact = user.getContact();
        Optional<User> checkUserContact = userRepository.findUserByContact(contact);
        if (checkUserContact.isPresent()){
            throw new Exception("User with this contact number already exists");
        }

        User currentUser = existingUser.get();
        utils.copyNonNullProperties(user, currentUser);

        if (user.getPassword() != null) {
            String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
            String hashPassword = utils.get_SHA_512_SecurePassword(user.getPassword(), currentTimestamp);
            System.out.println("hash => " + hashPassword);
            currentUser.setCreatedAt(currentTimestamp);
            currentUser.setPassword(hashPassword);
        }

        return userRepository.save(currentUser);
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
