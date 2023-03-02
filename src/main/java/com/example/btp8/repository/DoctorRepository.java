package com.example.btp8.repository;

import com.example.btp8.model.Doctor;
import com.example.btp8.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query(
            value = "SELECT * FROM doctor u WHERE u.email = ?1",
            nativeQuery = true
    )
    Optional<Doctor> findDoctorByEmail(String email);

    @Query(
            value = "SELECT * FROM doctor u WHERE u.contact = ?1",
            nativeQuery = true
    )
    Optional<Doctor> findDoctorByContact(String contact);

    @Query(
            value = "select * from doctor b where UPPER(b.email) like CONCAT('%',UPPER(?1),'%') ",
            nativeQuery = true
    )
    Page<Doctor> docFilter(String email, PageRequest id);
}
