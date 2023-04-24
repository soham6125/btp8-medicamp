package com.example.btp8.service;

import com.example.btp8.model.Answer;
import com.example.btp8.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer addAnswer(Answer answer) {
        Long currentTimestamp = Instant.now().toEpochMilli();
        answer.setCreatedAt(currentTimestamp);
        return answerRepository.save(answer);
    }

    public List<Answer> findAllAnswers(Long userID, String category) {
        return answerRepository.answerFilter(userID, category);
    }
}
