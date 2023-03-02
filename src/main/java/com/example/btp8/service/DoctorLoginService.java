package com.example.btp8.service;

import com.example.btp8.model.Doctor;
import com.example.btp8.model.DoctorLogin;
import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.repository.DoctorRepository;
import com.example.btp8.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoctorLoginService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorLoginService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor verifyDoctorLogin(DoctorLogin loginBody) throws Exception {
        String contact = loginBody.getContact();
        String password = loginBody.getPassword();
        if (contact != null) {
            Optional<Doctor> currentDoctor = doctorRepository.findDoctorByContact(contact);
            if (currentDoctor.isEmpty()) {
                throw new Exception("Invalid credentials");
            }
            String createdAtTimestamp = currentDoctor.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentDoctor.get().getPassword())) {
                return currentDoctor.get();
            } else {
                throw new Exception("Invalid credentials");
            }
        } else {
            String email = loginBody.getEmail();
            Optional<Doctor> currentDoctor = doctorRepository.findDoctorByEmail(email);
            if (currentDoctor.isEmpty()) {
                throw new Exception("Invalid credentials");
            }
            String createdAtTimestamp = currentDoctor.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentDoctor.get().getPassword())) {
                return currentDoctor.get();
            } else {
                throw new Exception("Invalid credentials");
            }
        }
    }

}
