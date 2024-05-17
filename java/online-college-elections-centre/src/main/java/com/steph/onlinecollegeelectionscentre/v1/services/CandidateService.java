package com.steph.onlinecollegeelectionscentre.v1.services;

import com.steph.onlinecollegeelectionscentre.v1.entities.Candidate;
import com.steph.onlinecollegeelectionscentre.v1.repositories.CandidateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public ResponseEntity<?> addCandidate(Candidate candidate, String requestId) {
        log.info("[" + requestId + "] is about to process request to add candidate to the repository");

        try {
            candidateRepository.save(candidate);

        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while persisting candidate: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(candidate);
    }


    public Optional<Candidate> findById(Long candidateId) {
        return candidateRepository.findById(candidateId);
    }
}
