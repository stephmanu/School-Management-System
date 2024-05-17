package com.steph.onlinecollegeelectionscentre.v1.controllers;

import com.steph.onlinecollegeelectionscentre.v1.entities.Candidate;
import com.steph.onlinecollegeelectionscentre.v1.entities.Vote;
import com.steph.onlinecollegeelectionscentre.v1.entities.Voter;/*
import com.steph.onlinecollegeelectionscentre.v1.enums.Position;*/
import com.steph.onlinecollegeelectionscentre.v1.models.LoginModel;
import com.steph.onlinecollegeelectionscentre.v1.services.CandidateService;
import com.steph.onlinecollegeelectionscentre.v1.services.VoterService;
import com.steph.onlinecollegeelectionscentre.v1.services.VotesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class VoterController {

    private final VoterService voterService;

    private final CandidateService candidateService;

    private final VotesService votesService;

    public VoterController(VoterService voterService,
                           CandidateService candidateService,
                           VotesService votesService) {

        this.voterService = voterService;
        this.candidateService = candidateService;
        this.votesService = votesService;

        Objects.requireNonNull(voterService, "Voter service is required");
        Objects.requireNonNull(candidateService, "Candidate service is required");
        Objects.requireNonNull(votesService, "Vote service is required");
    }

    // voter login/validation
    @PostMapping(value = "api/v1/login", produces = "application/json")
    public ResponseEntity<?> voterLogin (HttpServletRequest request, HttpServletResponse response,
                                         @RequestBody LoginModel LoginModel){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to login voter with username: "
                + LoginModel.getUsername());

        Optional<Voter> optionalVoter = voterService.validate(LoginModel, requestId);

        if (optionalVoter.isEmpty()){
            log.info("[" + requestId + "] request to login failed. Voter does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Voter does not exist.");
        }

        Voter voter = optionalVoter.get();

        log.info("[" + requestId + "] request to login voter with username: "
                + LoginModel.getUsername() + " was successful.");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(voter);
    }

    // cast vote
    @PostMapping(value = "api/v1/{voterId}/{candidateId}/castvote", produces = "application/json")
    public ResponseEntity<?> castVote (HttpServletRequest request, HttpServletResponse response,
                                       @PathVariable Long voterId, @PathVariable Long candidateId){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to cast vote for SRC President");

        Optional<Voter> optionalVoter = voterService.findById(voterId);

        if (optionalVoter.isEmpty()){
            log.info("[" + requestId + "] request to cast vote failed. Voter does not exist.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Voter does not exist.");
        }

        Voter voter = optionalVoter.get();

        Optional<Candidate> optionalCandidate = candidateService.findById(candidateId);
        if (optionalCandidate.isEmpty()){
            log.info("[" + requestId + "] request to cast vote failed. Candidate does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate does not exist");
        }

        Candidate candidate = optionalCandidate.get();

        // verify if candidate has voted for that position before
        List<Vote> voteList = voterService.findAll();

        for (Vote vote : voteList){
            if (vote.getVoter().getVoterId().equals(voterId)
                    && vote.getCandidate().getPosition().equals(candidate.getPosition())) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Voter has already cast vote for candidate position or category.");

            }
        }

        //set details for new vote
        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        vote.setCreatedOn(currentDateTime);

        //add new vote to the system
        ResponseEntity<?> addVoteResponse = votesService.addVote(vote, requestId);

        // add new vote to voter's list of votes and save
        voter.getVotes().add(vote);
        ResponseEntity<?> addVoterResponse = voterService.addUpdatedVoter(voter, requestId);

        // add new vote to candidate's list of votes and save
        candidate.getVotes().add(vote);
        candidateService.addCandidate(candidate, requestId);

        log.info("[" + requestId + "] request to vote for SRC Presidential candidate resulted in: " + addVoteResponse);

        return ResponseEntity.status(addVoterResponse.getStatusCode())
                .body(addVoterResponse.getBody());
    }

}
