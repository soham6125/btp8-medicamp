package com.example.btp8.service;

import com.example.btp8.dtos.DoctorResponseDto;
import com.example.btp8.exceptions.DoctorAlreadyExistsException;
import com.example.btp8.exceptions.DoctorNotFoundException;
import com.example.btp8.exceptions.InvalidCredentialsException;
import com.example.btp8.model.Doctor;
import com.example.btp8.model.Login;
import com.example.btp8.repository.DoctorRepository;
import com.example.btp8.utils.utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.Period.between;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    public DoctorResponseDto findDoctorByID(Long id) {
        Doctor currentDoctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return modelMapper.map(currentDoctor, DoctorResponseDto.class);
    }

    public Page<DoctorResponseDto> findAllDoctors(int page, int size, String email) {
        Page<Doctor> doctors = doctorRepository.docFilter(email, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return doctors.map(user -> modelMapper.map(user, DoctorResponseDto.class));
    }

    public DoctorResponseDto addDoctor(Doctor doctor) throws Exception {

//        Validations
        String email = doctor.getEmail();
        Optional<Doctor> checkDoctor = doctorRepository.findDoctorByEmail(email);
        if (checkDoctor.isPresent()) {
            throw new DoctorAlreadyExistsException("email");
        }
        String contact = doctor.getContact();
        Optional<Doctor> checkDoctorContact = doctorRepository.findDoctorByContact(contact);
        if (checkDoctorContact.isPresent()) {
            throw new DoctorAlreadyExistsException("contact number");
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

        doctor.setTimeSlots(utils.getAllTimeSlots());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }

    public DoctorResponseDto deleteDoctor(Long id) throws Exception {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.delete(doctor);
        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    public DoctorResponseDto editDoctor(Long id, @Valid Doctor doctor) throws Exception {

        Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
//        Validations
        String email = doctor.getEmail();
        Optional<Doctor> checkDoctor = doctorRepository.findDoctorByEmail(email);
        if (checkDoctor.isPresent()) {
            throw new DoctorAlreadyExistsException("email");
        }
        String contact = doctor.getContact();
        Optional<Doctor> checkDoctorContact = doctorRepository.findDoctorByContact(contact);
        if (checkDoctorContact.isPresent()) {
            throw new DoctorAlreadyExistsException("contact number");
        }

        utils.copyNonNullProperties(doctor, existingDoctor);

        if (doctor.getPassword() != null) {
            String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
            String hashPassword = utils.get_SHA_512_SecurePassword(doctor.getPassword(), currentTimestamp);
            System.out.println("hash => " + hashPassword);
            existingDoctor.setCreatedAt(currentTimestamp);
            existingDoctor.setPassword(hashPassword);
        }

        Doctor savedDoctor = doctorRepository.save(existingDoctor);
        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }

    public DoctorResponseDto verifyDoctorLogin(Login loginBody) throws Exception {
        String contact = loginBody.getContact();
        String password = loginBody.getPassword();
        Doctor doctor;
        if (contact != null) {
            doctor = doctorRepository.findDoctorByContact(contact).orElseThrow(InvalidCredentialsException::new);
        } else {
            String email = loginBody.getEmail();
            doctor = doctorRepository.findDoctorByEmail(email).orElseThrow(InvalidCredentialsException::new);
        }

        String createdAtTimestamp = doctor.getCreatedAt();
        String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
        if (newHash.equals(doctor.getPassword())) {
            return modelMapper.map(doctor, DoctorResponseDto.class);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public DoctorResponseDto updateAppointment(Long id, String time) throws Exception {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new Exception("Doctor not Found"));
        List<String> timeslots = doctor.getTimeSlots();
        String found = null;
        for (String s : timeslots) {
            if (s.equals(time)) {
                found = s;
                break;
            }
        }
        if (found == null) {
            timeslots.add(time);
        } else {
            timeslots.remove(found);
        }
        System.out.println(timeslots);
        doctor.setTimeSlots(timeslots);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorResponseDto.class);
    }
}
