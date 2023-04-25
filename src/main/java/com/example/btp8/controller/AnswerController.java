package com.example.btp8.controller;

import com.example.btp8.model.Answer;
import com.example.btp8.model.Associate;
import com.example.btp8.service.AnswerService;
import com.example.btp8.service.AssociateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/app/v1")
public class AnswerController {

    private final AnswerService answerService;
    private final AssociateService associateService;

    @Autowired
    public AnswerController(AnswerService answerService, AssociateService associateService) {
        this.answerService = answerService;
        this.associateService = associateService;
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> findAnswersPaginated(@RequestParam() Long userID,
                                                             @RequestParam() String category) {
        List<Answer> allAnswers = answerService.findAllAnswers(userID, category);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswers);
    }

    @PostMapping("/answer")
    public ResponseEntity<List<Associate>> addAnswerOfUser(@RequestBody @Valid Answer answerBody) {
        answerService.addAnswer(answerBody);
        List<Associate> associates = associateService.getAssociateByCategory(answerBody.getCategory());
        return ResponseEntity.status(HttpStatus.OK).body(associates);
    }

}
