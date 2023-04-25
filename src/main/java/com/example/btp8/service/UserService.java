package com.example.btp8.service;

import com.example.btp8.dtos.AppointmentDto;
import com.example.btp8.dtos.AppointmentResponseDto;
import com.example.btp8.dtos.UserResponseDto;
import com.example.btp8.exceptions.DoctorNotFoundException;
import com.example.btp8.exceptions.InvalidCredentialsException;
import com.example.btp8.exceptions.UserAlreadyExistsException;
import com.example.btp8.exceptions.UserNotFoundException;
import com.example.btp8.model.Appointment;
import com.example.btp8.model.Doctor;
import com.example.btp8.model.Login;
import com.example.btp8.model.User;
import com.example.btp8.repository.AppointmentRepository;
import com.example.btp8.repository.DoctorRepository;
import com.example.btp8.repository.UserRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.Period.between;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    public Map<String, String> healthCheck() {
        Map<String, String> res = new HashMap<>();
        res.put("message", "Welcome to MediCamp");
        return res;
    }

    public UserResponseDto findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public Page<UserResponseDto> findAllUsers(int page, int size, String email) {
        Page<User> users = userRepository.userFilter(email, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return users.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    public UserResponseDto addUser(@Valid User user) {

        String email = user.getEmail();
        Optional<User> checkUser = userRepository.findUserByEmail(email);
        if (checkUser.isPresent()) {
            throw new UserAlreadyExistsException("email");
        }

        String contact = user.getContact();
        Optional<User> checkUserContact = userRepository.findUserByContact(contact);
        if (checkUserContact.isPresent()) {
            throw new UserAlreadyExistsException("contact number");
        }

        UUID randomUUID = UUID.randomUUID();
        String tokenID = randomUUID.toString().replaceAll("-", "");
        user.setToken(tokenID);

        int age = between(user.getDob(), LocalDate.now()).getYears();
        user.setAge(age);

        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
        String hashPassword = utils.get_SHA_512_SecurePassword(user.getPassword(), currentTimestamp);
        user.setPassword(hashPassword);
        user.setCreatedAt(currentTimestamp);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    public UserResponseDto deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto editUser(Long id, @Valid User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

//        Validation checks
        String email = user.getEmail();
        Optional<User> checkUser = userRepository.findUserByEmail(email);
        if (checkUser.isPresent()) {
            throw new UserAlreadyExistsException("email");
        }
        String contact = user.getContact();
        Optional<User> checkUserContact = userRepository.findUserByContact(contact);
        if (checkUserContact.isPresent()) {
            throw new UserAlreadyExistsException("contact number");
        }

        utils.copyNonNullProperties(user, existingUser);
        if (user.getPassword() != null) {
            String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
            String hashPassword = utils.get_SHA_512_SecurePassword(user.getPassword(), currentTimestamp);
            System.out.println("hash => " + hashPassword);
            existingUser.setCreatedAt(currentTimestamp);
            existingUser.setPassword(hashPassword);
        }
        User savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    public UserResponseDto verifyUserLogin(Login loginBody) throws Exception {
        String contact = loginBody.getContact();
        String password = loginBody.getPassword();
        User currentUser;
        if (contact != null) {
            currentUser = userRepository.findUserByContact(contact).orElseThrow(InvalidCredentialsException::new);
        } else {
            String email = loginBody.getEmail();
            currentUser = userRepository.findUserByEmail(email).orElseThrow(InvalidCredentialsException::new);
        }
        String createdAtTimestamp = currentUser.getCreatedAt();
        String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
        if (newHash.equals(currentUser.getPassword())) {
            return modelMapper.map(currentUser, UserResponseDto.class);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public AppointmentResponseDto createNewAppointment(Long userId, AppointmentDto appointmentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElseThrow(() -> new DoctorNotFoundException(appointmentDto.getDoctorId()));
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        appointment.setUser(user);
        appointment.setDoctor(doctor);
        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
        appointment.setCreatedAt(currentTimestamp);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return modelMapper.map(savedAppointment, AppointmentResponseDto.class);
    }
}
