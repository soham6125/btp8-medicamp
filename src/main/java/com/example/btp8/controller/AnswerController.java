package com.example.btp8.controller;

import com.example.btp8.dtos.DoctorResponseDto;
import com.example.btp8.dtos.ErrorResponseDto;
import com.example.btp8.exceptions.DoctorNotFoundException;
import com.example.btp8.model.Answer;
import com.example.btp8.model.Associate;
import com.example.btp8.model.Doctor;
import com.example.btp8.service.AnswerService;
import com.example.btp8.service.AssociateService;
import com.example.btp8.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ModelMapper modelMapper;


    @Autowired
    public AnswerController(AnswerService answerService, AssociateService associateService, DoctorService doctorService, ModelMapper modelMapper) {
        this.answerService = answerService;
        this.associateService = associateService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> findAnswersPaginated(@RequestParam() Long userID,
                                                             @RequestParam() String category) {
        List<Answer> allAnswers = answerService.findAllAnswers(userID, category);
        return ResponseEntity.status(HttpStatus.OK).body(allAnswers);
    }

    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> addAnswerOfUser(@RequestBody @Valid Answer answerBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(answerService.addAnswer(answerBody));
    }

}
