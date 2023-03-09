package com.example.btp8.controller;

import com.example.btp8.model.Answer;
import com.example.btp8.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/app/v1")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/answer")
    public ResponseEntity<Map<String, Object>> findAnswersPaginated(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @RequestParam() Long userID) {
        Map<String, Object> response = new HashMap<>();
        Page<Answer> userPaginated = answerService.findAllAnswers(page, size, userID);
        response.put("data", userPaginated.getContent());
        response.put("currentPage", userPaginated.getNumber());
        response.put("totalItems", userPaginated.getTotalElements());
        response.put("totalPages", userPaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/answer")
    public ResponseEntity<Answer> addAnswerOfUser(@RequestBody @Valid Answer answerBody){
        return ResponseEntity.status(HttpStatus.OK).body(answerService.addAnswer(answerBody));
    }

}
