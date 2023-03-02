package com.example.btp8.controller;

import com.example.btp8.service.QuestionsService;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/app/v1")
public class QuestionsController {

    private final QuestionsService questionsService;

    public QuestionsController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping("/questions/{category}")
    public Object fetchQuestions(@PathVariable("category") String category) throws IOException, ParseException {
        return ResponseEntity.status(HttpStatus.OK).body(questionsService.retrieveQuestions(category));
    }
}
