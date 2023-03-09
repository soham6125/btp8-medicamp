package com.example.btp8.service;

import com.example.btp8.model.Answer;
import com.example.btp8.model.User;
import com.example.btp8.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;

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

    public Page<Answer> findAllAnswers(int page, int size, Long userID){
        return answerRepository.answerFilter(userID, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

}
