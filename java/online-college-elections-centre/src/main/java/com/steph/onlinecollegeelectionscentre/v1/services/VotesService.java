package com.steph.onlinecollegeelectionscentre.v1.services;

import com.steph.onlinecollegeelectionscentre.v1.entities.Vote;
import com.steph.onlinecollegeelectionscentre.v1.repositories.VotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class VotesService {

    private final VotesRepository votesRepository;

    public VotesService(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;

        Objects.requireNonNull(votesRepository, "Vote repository is required");
    }

    public ResponseEntity<?> addVote (Vote vote, String requestId){
        log.info("[" + requestId + "] is about to process request to add new vote to the repository");

        try {
            votesRepository.save(vote);
        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while persisting new vote: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(vote);
    }
}
