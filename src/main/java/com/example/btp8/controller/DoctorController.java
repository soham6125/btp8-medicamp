package com.example.btp8.controller;

import com.example.btp8.model.Doctor;
import com.example.btp8.model.DoctorLogin;
import com.example.btp8.service.DoctorLoginService;
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
    private final DoctorLoginService doctorLoginService;

    public DoctorController(DoctorService doctorService, DoctorLoginService doctorLoginService) {
        this.doctorService = doctorService;
        this.doctorLoginService = doctorLoginService;
    }

    @GetMapping("/doctor/all")
    public ResponseEntity<Map<String, Object>> findDoctorPaginated(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<Doctor> doctorPaginated = doctorService.findAllDoctors(page, size, email);
        response.put("data", doctorPaginated.getContent());
        response.put("currentPage", doctorPaginated.getNumber());
        response.put("totalItems", doctorPaginated.getTotalElements());
        response.put("totalPages", doctorPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.findDoctorByID(id));
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody Doctor doctor) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.addDoctor(doctor));
    }

    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.deleteDoctor(id));
    }

    @PatchMapping("/doctor/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable("id") Long id, @Valid @RequestBody Doctor doctor) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.editDoctor(id, doctor));
    }

    @PostMapping("/doctor/login")
    public ResponseEntity<Doctor> login(@Valid @RequestBody DoctorLogin loginBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(doctorLoginService.verifyDoctorLogin(loginBody));
    }
}
