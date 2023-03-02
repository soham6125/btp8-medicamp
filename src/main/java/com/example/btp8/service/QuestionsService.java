package com.example.btp8.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class QuestionsService {

    public Object retrieveQuestions(String category) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("C:\\Users\\SOHAM\\Desktop\\btp8\\btp8\\src\\main\\java\\com\\example\\btp8\\Questions.json"));
        Object res = jsonObject.get(category);
        return res;
    }
}
