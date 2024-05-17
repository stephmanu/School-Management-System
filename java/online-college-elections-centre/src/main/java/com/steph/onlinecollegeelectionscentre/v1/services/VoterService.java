package com.steph.onlinecollegeelectionscentre.v1.services;

import com.steph.onlinecollegeelectionscentre.v1.entities.Voter;
import com.steph.onlinecollegeelectionscentre.v1.entities.Vote;
import com.steph.onlinecollegeelectionscentre.v1.models.LoginModel;
import com.steph.onlinecollegeelectionscentre.v1.repositories.VoterRepository;
import com.steph.onlinecollegeelectionscentre.v1.repositories.VotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class VoterService {
    private final VotesRepository votesRepository;
    private final VoterRepository voterRepository;

    public VoterService(VoterRepository voterRepository,
                        VotesRepository votesRepository) {
        this.voterRepository = voterRepository;

        Objects.requireNonNull(voterRepository, "Voter repository is required");
        this.votesRepository = votesRepository;
    }

    public ResponseEntity<?> addVoter(Voter voter, String requestId) {
        log.info("[" + requestId + "] is about to process request to add voter with username: "
                + voter.getUsername() + " to the repository.");

        if (voterRepository.existsVoterByUsername(voter.getUsername())
                || voterRepository.existsVoterByPassword(voter.getPassword())){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username or password already exist.");
        }

        try{
            voterRepository.save(voter);

        } catch (Exception e) {

            throw new IllegalStateException("An error occurred while persisting voter: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(voter);
    }


    public ResponseEntity<?> addUpdatedVoter(Voter voter, String requestId) {
        log.info("[" + requestId + "] is about to process request to add updated voter with username: "
                + voter.getUsername() + " to the repository.");

        try{
            voterRepository.save(voter);

        } catch (Exception e) {

            throw new IllegalStateException("An error occurred while persisting voter: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(voter);
    }


    public Optional<Voter> validate(LoginModel LoginModel, String requestId) {

        return voterRepository.findByUsernameAndPassword(LoginModel.getUsername(), LoginModel.getPassword());
    }

    public Optional<Voter> findById(Long voterId) {
        return voterRepository.findById(voterId);
    }


    public List<Vote> findAll() {
        return votesRepository.findAll();
    }
}
