package com.example.btp8.controller;

import com.example.btp8.model.Answer;
import com.example.btp8.model.Associate;
import com.example.btp8.model.Doctor;
import com.example.btp8.service.AnswerService;
import com.example.btp8.service.AssociateService;
import com.example.btp8.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/app/v1")
public class AnswerController {

    private final AnswerService answerService;
    private final AssociateService associateService;
    private final DoctorService doctorService;

    @Autowired
    public AnswerController(AnswerService answerService, AssociateService associateService, DoctorService doctorService) {
        this.answerService = answerService;
        this.associateService = associateService;
        this.doctorService = doctorService;
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> findAnswersPaginated(@RequestParam() Long userID,
                                                             @RequestParam() String category) {
        List<Answer> allAnswers = answerService.findAllAnswers(userID, category);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswers);
    }

    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> addAnswerOfUser(@RequestBody @Valid Answer answerBody) {
        Answer currentAnswer = answerService.addAnswer(answerBody);
        List<Associate> associates = associateService.getAssociateByCategory(answerBody.getCategory());
        List<Doctor> doctors = doctorService.getDcotorByCategory(answerBody.getCategory());
        Map<String, Object> response = new HashMap<>();
        response.put("associates", associates);
        response.put("doctors", doctors);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
