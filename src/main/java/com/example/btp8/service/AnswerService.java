package com.example.btp8.service;

import com.example.btp8.model.Answer;
import com.example.btp8.model.Associate;
import com.example.btp8.repository.AnswerRepository;
import com.example.btp8.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AssociateRepository associateRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, AssociateRepository associateRepository) {
        this.answerRepository = answerRepository;
        this.associateRepository = associateRepository;
    }

    public Answer addAnswer(Answer answer) {
        Long currentTimestamp = Instant.now().toEpochMilli();
        answer.setCreatedAt(currentTimestamp);
        return answerRepository.save(answer);
    }

    public List<Answer> findAllAnswers(Long userID, String category){
        return answerRepository.answerFilter(userID, category);
    }

}
