package com.example.btp8.repository;

import com.example.btp8.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            value = "SELECT * FROM user u WHERE u.email = ?1",
            nativeQuery = true
    )
    Optional<User> findUserByEmail(String email);

    @Query(
            value = "SELECT * FROM user u WHERE u.contact = ?1",
            nativeQuery = true
    )
    Optional<User> findUserByContact(String contact);

    @Query(
            value = "select * from user b where UPPER(b.email) like CONCAT('%',UPPER(?1),'%') ",
            nativeQuery = true
    )
    Page<User> userFilter(String email, PageRequest id);
}
