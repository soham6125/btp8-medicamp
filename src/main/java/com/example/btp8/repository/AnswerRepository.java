package com.example.btp8.repository;

import com.example.btp8.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(
            value = "select * from answer b where UPPER(b.userID) like CONCAT('%',UPPER(?1),'%') and UPPER(b.category) like CONCAT('%',UPPER(?2),'%')",
            nativeQuery = true
    )
    List<Answer> answerFilter(Long userID, String category);

}


