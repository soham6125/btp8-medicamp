package com.example.btp8.service;

import com.example.btp8.model.Doctor;
import com.example.btp8.model.Login;
import com.example.btp8.repository.DoctorRepository;
import com.example.btp8.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static java.time.Period.between;
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor findDoctorByID(Long id) {
        Optional<Doctor> currentDoctor = doctorRepository.findById(id);
        if (currentDoctor.isEmpty()) {
            throw new RuntimeException("Doctor with ID " + id + " does not exist");
        }
        return currentDoctor.get();
    }

    public Page<Doctor> findAllDoctors(int page, int size, String email) {
        return doctorRepository.docFilter(email, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    public Doctor addDoctor(Doctor doctor) throws Exception {

        String email = doctor.getEmail();
        Optional<Doctor> checkDoctor = doctorRepository.findDoctorByEmail(email);
        if (checkDoctor.isPresent()){
            throw new Exception("Doctor with this email " + email + " already exists");
        }

        String contact = doctor.getContact();
        Optional<Doctor> checkDoctorContact = doctorRepository.findDoctorByContact(contact);
        if (checkDoctorContact.isPresent()){
            throw new Exception("User with this contact number already exists");
        }

        UUID randomUUID = UUID.randomUUID();
        String tokenID = randomUUID.toString().replaceAll("-", "");
        doctor.setToken(tokenID);

        int age = between(doctor.getDob(), LocalDate.now()).getYears();
        doctor.setAge(age);

        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
        doctor.setCreatedAt(currentTimestamp);

        String hashPassword = utils.get_SHA_512_SecurePassword(doctor.getPassword(), currentTimestamp);
        System.out.println("hash => " + hashPassword);
        doctor.setPassword(hashPassword);

        return doctorRepository.save(doctor);
    }

    public Doctor deleteDoctor(Long id) throws Exception {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isEmpty()) {
            throw new Exception("Doctor with given ID does not exist");
        }

        Doctor currentDoctor = doctor.get();
        doctorRepository.delete(currentDoctor);
        return currentDoctor;
    }

    public Doctor editDoctor(Long id, @Valid Doctor doctor) throws Exception {

        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if (existingDoctor.isEmpty()) {
            throw new RuntimeException("Doctor with id " + id + " does not exist");
        }

        String email = doctor.getEmail();
        Optional<Doctor> checkDoctor = doctorRepository.findDoctorByEmail(email);
        if (checkDoctor.isPresent()){
            throw new Exception("User with this email ID already exists");
        }

        String contact = doctor.getContact();
        Optional<Doctor> checkDoctorContact = doctorRepository.findDoctorByContact(contact);
        if (checkDoctorContact.isPresent()){
            throw new Exception("User with this contact number already exists");
        }

        Doctor currentDoctor = existingDoctor.get();
        utils.copyNonNullProperties(doctor, currentDoctor);

        if (doctor.getPassword() != null) {
            String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
            String hashPassword = utils.get_SHA_512_SecurePassword(doctor.getPassword(), currentTimestamp);
            System.out.println("hash => " + hashPassword);
            currentDoctor.setCreatedAt(currentTimestamp);
            currentDoctor.setPassword(hashPassword);
        }

        return doctorRepository.save(currentDoctor);
    }

    public Doctor verifyDoctorLogin(Login loginBody) throws Exception {
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
