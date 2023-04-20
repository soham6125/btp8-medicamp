package com.example.btp8.repository;

import com.example.btp8.model.Associate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, Long> {

    @Query(
            value = "SELECT * FROM associate u WHERE u.email = ?1",
            nativeQuery = true
    )
    Optional<Associate> findAssociateByEmail(String email);

    @Query(
            value = "SELECT * FROM associate u WHERE u.contact = ?1",
            nativeQuery = true
    )
    Optional<Associate> findAssociateByContact(String contact);

    @Query(
            value = "SELECT * FROM associate b WHERE UPPER(b.area_of_expertise) like CONCAT('%',UPPER(?1),'%') order by rand() limit 2;",
            nativeQuery = true
    )
    List<Associate> findAssociateByCategory(String category);

    @Query(
            value = "select * from associate b where UPPER(b.email) like CONCAT('%',UPPER(?1),'%') ",
            nativeQuery = true
    )
    Page<Associate> associateFilter(String email, PageRequest id);
}
