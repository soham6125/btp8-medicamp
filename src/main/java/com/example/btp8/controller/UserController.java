package com.example.btp8.controller;

import com.example.btp8.dtos.AppointmentDto;
import com.example.btp8.dtos.AppointmentResponseDto;
import com.example.btp8.dtos.ErrorResponseDto;
import com.example.btp8.dtos.UserResponseDto;
import com.example.btp8.exceptions.DoctorNotFoundException;
import com.example.btp8.exceptions.UserNotFoundException;
import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/app/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.healthCheck());
    }

    @GetMapping("/user/all")
    public ResponseEntity<Map<String, Object>> findUserPaginated(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<UserResponseDto> userPaginated = userService.findAllUsers(page, size, email);
        response.put("data", userPaginated.getContent());
        response.put("currentPage", userPaginated.getNumber());
        response.put("totalItems", userPaginated.getTotalElements());
        response.put("totalPages", userPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(id));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.editUser(id, user));
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody Login loginBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.verifyUserLogin(loginBody));
    }

    @PostMapping("/user/{id}/appointment")
    public ResponseEntity<AppointmentResponseDto> makeAppointment(@PathVariable("id") Long userId, @RequestBody AppointmentDto appointmentDto) {
        AppointmentResponseDto appointmentResponse = userService.createNewAppointment(userId, appointmentDto);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
    }

    @ExceptionHandler({IllegalArgumentException.class, UserNotFoundException.class, DoctorNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptions(Exception e) {
        if (e instanceof UserNotFoundException || e instanceof DoctorNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(e.getMessage()));
        }
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
