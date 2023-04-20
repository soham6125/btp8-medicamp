package com.example.btp8.controller;

import com.example.btp8.model.Associate;
import com.example.btp8.model.Login;
import com.example.btp8.service.AssociateService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

@RestController
@RequestMapping(path = "/app/v1")
public class AssociateController {

    private final AssociateService associateService;

    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @GetMapping("/associate/all")
    public ResponseEntity<Map<String, Object>> findAssociatePaginated(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "5") int size,
                                                                      @RequestParam(defaultValue = "") String email) {
        Map<String, Object> response = new HashMap<>();
        Page<Associate> associatePaginated = associateService.findAllAssociates(page, size, email);
        response.put("data", associatePaginated.getContent());
        response.put("currentPage", associatePaginated.getNumber());
        response.put("totalItems", associatePaginated.getTotalElements());
        response.put("totalPages", associatePaginated.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/associate/{id}")
    public ResponseEntity<Associate> getAssociate(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(associateService.findAssociate(id));
    }

    @GetMapping("/associate")
    public ResponseEntity<List<Associate>> getAssociateCategory(@RequestParam() String category){
        System.out.println(category);
        return ResponseEntity.status(HttpStatus.OK).body(associateService.getAssociateByCategory(category));
    }

    @PostMapping("/associate")
    public ResponseEntity<Associate> addAssociate(@Valid @RequestBody Associate associate) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.addAssociate(associate));
    }

    @DeleteMapping("/associate/{id}")
    public ResponseEntity<Associate> deleteAssociate(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.deleteAssociate(id));
    }

    @PatchMapping("/associate/{id}")
    public ResponseEntity<Associate> updateAssociate(@PathVariable("id") Long id, @Valid @RequestBody Associate associate) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.editAssociate(id, associate));
    }

    @PostMapping("/associate/login")
    public ResponseEntity<Associate> login(@Valid @RequestBody Login loginBody) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.verifyAssociateLogin(loginBody));
    }

}
