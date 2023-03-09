package com.example.btp8.repository;

import com.example.btp8.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(
            value = "select * from answer b where UPPER(b.userID) like CONCAT('%',UPPER(?1),'%') ",
            nativeQuery = true
    )
    Page<Answer> answerFilter(Long userID, PageRequest id);

}


