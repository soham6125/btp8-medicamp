package com.example.btp8.model;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private Integer age;
    @NotNull
    private String contact;
    @NotNull
    private String email;
    @NotNull
    private String address;
    @NotNull
    private LocalDate dob;
    @NotNull
    private String token;
    @NotNull
    private String password;
    @NotNull
    private String createdAt;

    public User(String name, Integer age, String contact, String email, String address, LocalDate dob, String token, String password, String createdAt) {
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.token = token;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
