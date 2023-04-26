package com.example.btp8.service;

import com.example.btp8.dtos.DoctorResponseDto;
import com.example.btp8.model.Answer;
import com.example.btp8.model.Associate;
import com.example.btp8.model.Doctor;
import com.example.btp8.repository.AnswerRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AssociateService associateService;
    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, AssociateService associateService, DoctorService doctorService, ModelMapper modelMapper) {
        this.answerRepository = answerRepository;
        this.associateService = associateService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    public Map<String, Object> addAnswer(Answer answer) throws Exception {
        Long maxScore = answer.getMaxScore();
        Long score = answer.getScore();
        if (score > maxScore) {
            throw new Exception("Score can't exceed max score");
        }
        Long currentTimestamp = Instant.now().toEpochMilli();
        answer.setCreatedAt(currentTimestamp);
        answerRepository.save(answer);
        List<Associate> associates = associateService.getAssociateByCategory(answer.getCategory());
        List<Doctor> doctors = doctorService.getDoctorByCategory(answer.getCategory());
        List<DoctorResponseDto> doctorResponse = doctors.stream().map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class)).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("associates", associates);
        response.put("doctors", doctorResponse);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/main/java/com/example/btp8/Books.json"));
        Object books = jsonObject.get(answer.getCategory());
        response.put("books", books);

        JSONObject jsonObject2 = (JSONObject) jsonParser.parse(new FileReader("src/main/java/com/example/btp8/Articles.json"));
        Object articles = jsonObject2.get(answer.getCategory());
        response.put("articles", articles);

        return response;
    }

    public List<Answer> findAllAnswers(Long userID, String category) {
        return answerRepository.answerFilter(userID, category);
    }
}