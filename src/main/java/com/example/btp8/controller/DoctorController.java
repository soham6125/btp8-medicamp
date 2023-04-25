package com.example.btp8.controller;

import com.example.btp8.dtos.DoctorResponseDto;
import com.example.btp8.dtos.ErrorResponseDto;
import com.example.btp8.exceptions.DoctorNotFoundException;
import com.example.btp8.exceptions.UserNotFoundException;
import com.example.btp8.model.Doctor;
import com.example.btp8.model.Login;
import com.example.btp8.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/app/v1")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctor/all")
    public ResponseEntity<Map<String, Object>> findDoctorPaginated(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<DoctorResponseDto> doctorPaginated = doctorService.findAllDoctors(page, size, email);
        response.put("data", doctorPaginated.getContent());
        response.put("currentPage", doctorPaginated.getNumber());
        response.put("totalItems", doctorPaginated.getTotalElements());
        response.put("totalPages", doctorPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctor(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.findDoctorByID(id));
    }

    @PostMapping("/doctor")
    public ResponseEntity<DoctorResponseDto> addDoctor(@Valid @RequestBody Doctor doctor) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.addDoctor(doctor));
    }

    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<DoctorResponseDto> deleteDoctor(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.deleteDoctor(id));
    }

    @PatchMapping("/doctor/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(@PathVariable("id") Long id, @Valid @RequestBody Doctor doctor) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.editDoctor(id, doctor));
    }

    @PatchMapping("/doctor/{id}/appointment")
    public ResponseEntity<DoctorResponseDto> updateTimeSlots(@PathVariable("id") Long id, @RequestBody String timeslot) throws Exception {
        System.out.println(timeslot);
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.updateAppointment(id, timeslot));
    }

    @PostMapping("/doctor/login")
    public ResponseEntity<DoctorResponseDto> login(@Valid @RequestBody Login loginBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.verifyDoctorLogin(loginBody));
    }

    @ExceptionHandler({IllegalArgumentException.class, DoctorNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptions(Exception e) {
        if (e instanceof DoctorNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(e.getMessage()));
        }
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
