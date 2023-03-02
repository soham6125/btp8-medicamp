package com.example.btp8.controller;

import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.service.LoginService;
import com.example.btp8.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(path = "/app/v1")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.healthCheck());
    }

    @GetMapping("/user/all")
    public ResponseEntity<Map<String, Object>> findUserPaginated(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<User> userPaginated = userService.findAllUsers(page, size, email);
        response.put("data", userPaginated.getContent());
        response.put("currentPage", userPaginated.getNumber());
        response.put("totalItems", userPaginated.getTotalElements());
        response.put("totalPages", userPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(id));
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.editUser(id, user));
    }

    @PostMapping("/user/login")
    public ResponseEntity<User> login(@Valid @RequestBody Login loginBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(loginService.verifyUserLogin(loginBody));
    }

}
