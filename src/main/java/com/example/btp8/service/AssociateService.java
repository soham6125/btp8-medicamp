package com.example.btp8.service;

import com.example.btp8.model.Associate;
import com.example.btp8.model.Login;
import com.example.btp8.repository.AssociateRepository;
import com.example.btp8.utils.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static java.time.Period.between;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;
    @Autowired
    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    public Associate findAssociate(Long id){
        Optional<Associate> associate = associateRepository.findById(id);
        if (associate.isEmpty()) {
            throw new RuntimeException("Associate with given ID does not exist");
        }
        return associate.get();
    }

    public Page<Associate> findAllAssociates(int page, int size, String email){
        return associateRepository.associateFilter(email, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    public Associate addAssociate(@Valid Associate associate) throws Exception {

        String email = associate.getEmail();
        Optional<Associate> checkAssociate = associateRepository.findAssociateByEmail(email);
        if (checkAssociate.isPresent()){
            throw new Exception("Associate with this email ID already exists");
        }

        String contact = associate.getContact();
        Optional<Associate> checkAssociateContact = associateRepository.findAssociateByContact(contact);
        if (checkAssociateContact.isPresent()){
            throw new Exception("Associate with this contact number already exists");
        }

        UUID randomUUID = UUID.randomUUID();
        String tokenID = randomUUID.toString().replaceAll("-", "");
        associate.setToken(tokenID);

        int age = between(associate.getDob(), LocalDate.now()).getYears();
        associate.setAge(age);

        String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
        String hashPassword = utils.get_SHA_512_SecurePassword(associate.getPassword(), currentTimestamp);
        associate.setPassword(hashPassword);
        associate.setCreatedAt(currentTimestamp);
        return associateRepository.save(associate);
    }

    public Associate deleteAssociate(Long id) throws Exception {
        Optional<Associate> associate = associateRepository.findById(id);
        if (associate.isEmpty()) {
            throw new Exception("Associate with given ID does not exist");
        }
        Associate currentAssociate = associate.get();
        associateRepository.delete(currentAssociate);
        return currentAssociate;
    }

    public Associate editAssociate(Long id, @Valid Associate associate) throws Exception {

        Optional<Associate> existingAssociate = associateRepository.findById(id);
        if (existingAssociate.isEmpty()) {
            throw new RuntimeException("Associate with id " + id + " does not exist");
        }

        String email = associate.getEmail();
        Optional<Associate> checkAssociate = associateRepository.findAssociateByEmail(email);
        if (checkAssociate.isPresent()){
            throw new Exception("Associate with this email ID already exists");
        }

        String contact = associate.getContact();
        Optional<Associate> checkAssociateContact = associateRepository.findAssociateByContact(contact);
        if (checkAssociateContact.isPresent()){
            throw new Exception("Associate with this contact number already exists");
        }

        Associate currentAssociate = existingAssociate.get();
        utils.copyNonNullProperties(associate, currentAssociate);
        if (associate.getPassword() != null) {
            String currentTimestamp = String.valueOf(Instant.now().toEpochMilli());
            String hashPassword = utils.get_SHA_512_SecurePassword(associate.getPassword(), currentTimestamp);
//            System.out.println("hash => " + hashPassword);
            currentAssociate.setCreatedAt(currentTimestamp);
            currentAssociate.setPassword(hashPassword);
        }
        return associateRepository.save(currentAssociate);
    }

    public Associate verifyAssociateLogin(Login loginBody) throws Exception {
        String contact = loginBody.getContact();
        String password = loginBody.getPassword();

        if (contact != null) {

            Optional<Associate> currentAssociate = associateRepository.findAssociateByContact(contact);
            if (currentAssociate.isEmpty()) {
                throw new Exception("Invalid credentials");
            }

            String createdAtTimestamp = currentAssociate.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentAssociate.get().getPassword())) {
                return currentAssociate.get();
            } else {
                throw new Exception("Invalid credentials");
            }

        } else {

            String email = loginBody.getEmail();
            Optional<Associate> currentAssociate = associateRepository.findAssociateByEmail(email);
            if (currentAssociate.isEmpty()) {
                throw new Exception("Invalid credentials");
            }

            String createdAtTimestamp = currentAssociate.get().getCreatedAt();
            String newHash = utils.get_SHA_512_SecurePassword(password, createdAtTimestamp);
            if (newHash.equals(currentAssociate.get().getPassword())) {
                return currentAssociate.get();
            } else {
                throw new Exception("Invalid credentials");
            }

        }
    }
}
