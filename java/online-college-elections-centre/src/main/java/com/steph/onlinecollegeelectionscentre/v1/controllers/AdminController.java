package com.steph.onlinecollegeelectionscentre.v1.controllers;

import com.steph.onlinecollegeelectionscentre.v1.entities.Candidate;
import com.steph.onlinecollegeelectionscentre.v1.entities.Vote;
import com.steph.onlinecollegeelectionscentre.v1.entities.Voter;
import com.steph.onlinecollegeelectionscentre.v1.enums.Position;
import com.steph.onlinecollegeelectionscentre.v1.models.AddCandidateModel;
import com.steph.onlinecollegeelectionscentre.v1.models.AddVoterModel;
import com.steph.onlinecollegeelectionscentre.v1.services.CandidateService;
import com.steph.onlinecollegeelectionscentre.v1.services.VoterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class AdminController {

    private final CandidateService candidateService;

    private final VoterService voterService;

    public AdminController(CandidateService candidateService, VoterService voterService) {
        this.candidateService = candidateService;
        this.voterService = voterService;

        Objects.requireNonNull(candidateService, "Candidate service is required");
        Objects.requireNonNull(voterService, "Voter service is required");
    }

    // adding src presidential candidate
    @PostMapping(value = "api/v1/admin/candidates/srcpresident/add", produces = "application/json")
    public ResponseEntity<?> addSrcPresident (HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody AddCandidateModel addCandidateModel){

        String requestId = request.getRequestedSessionId();

        log.info("[" + requestId + "] is about to process request to add SRC Presidential candidate to the system.");

        Candidate candidate = new Candidate();

        if (addCandidateModel.getFirstName() != null && !addCandidateModel.getFirstName().isEmpty()){

            candidate.setFirstName(addCandidateModel.getFirstName());
        }

        if (addCandidateModel.getLastName() != null && !addCandidateModel.getLastName().isEmpty()){

            candidate.setLastName(addCandidateModel.getLastName());
        }

        candidate.setPosition(Position.SRC_PRESIDENT);

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        candidate.setCreatedOn(currentDateTime);
        candidate.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addCandidateResponse = candidateService.addCandidate(candidate, requestId);

        log.info("[" + requestId + "] request to add SRC Presidential candidate resulted in: "
                + addCandidateResponse);

        return ResponseEntity.status(addCandidateResponse.getStatusCode())
                .body(addCandidateResponse.getBody());
    }

    // adding financial secretary
    @PostMapping(value = "api/v1/admin/candidates/financialsecretary/add", produces = "application/json")
    public ResponseEntity<?> addFinancialSecretary (HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody AddCandidateModel addCandidateModel){

        String requestId = request.getSession().getId();

        log.info("[" + requestId + "] is about to process request to add SRC Financial Secretary candidate to the system.");

        Candidate candidate = new Candidate();

        if (addCandidateModel.getFirstName() != null && !addCandidateModel.getFirstName().isEmpty()){

            candidate.setFirstName(addCandidateModel.getFirstName());
        }

        if (addCandidateModel.getLastName() != null && !addCandidateModel.getLastName().isEmpty()){

            candidate.setLastName(addCandidateModel.getLastName());
        }

        candidate.setPosition(Position.FINANCIAL_SECRETARY);

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        candidate.setCreatedOn(currentDateTime);
        candidate.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addCandidateResponse = candidateService.addCandidate(candidate, requestId);

        log.info("[" + requestId + "] request to add Financial Secretary candidate resulted in: "
                + addCandidateResponse);

        return ResponseEntity.status(addCandidateResponse.getStatusCode())
                .body(addCandidateResponse.getBody());
    }

    // adding general secretary
    @PostMapping(value = "api/v1/admin/candidates/generalsecretary/add", produces = "application/json")
    public ResponseEntity<?> addGeneralSecretary (HttpServletRequest request, HttpServletResponse response,
                                                    @RequestBody AddCandidateModel addCandidateModel){

        String requestId = request.getSession().getId();

        log.info("[" + requestId + "] is about to process request to add SRC General Secretary candidate to the system.");

        Candidate candidate = new Candidate();

        if (addCandidateModel.getFirstName() != null && !addCandidateModel.getFirstName().isEmpty()){

            candidate.setFirstName(addCandidateModel.getFirstName());
        }

        if (addCandidateModel.getLastName() != null && !addCandidateModel.getLastName().isEmpty()){

            candidate.setLastName(addCandidateModel.getLastName());
        }

        candidate.setPosition(Position.GENERAL_SECRETARY);

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        candidate.setCreatedOn(currentDateTime);
        candidate.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addCandidateResponse = candidateService.addCandidate(candidate, requestId);

        log.info("[" + requestId + "] request to add General Secretary candidate resulted in: "
                + addCandidateResponse);

        return ResponseEntity.status(addCandidateResponse.getStatusCode())
                .body(addCandidateResponse.getBody());
    }

    // adding women's commissioner
    @PostMapping(value = "api/v1/admin/candidates/womenscommissioner/add", produces = "application/json")
    public ResponseEntity<?> addWomensCommissioner (HttpServletRequest request, HttpServletResponse response,
                                                    @RequestBody AddCandidateModel addCandidateModel){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process a request to add SRC Women's Commissioner candidate to the system.");

        Candidate candidate = new Candidate();

        if (addCandidateModel.getFirstName() != null && !addCandidateModel.getFirstName().isEmpty()){

            candidate.setFirstName(addCandidateModel.getFirstName());
        }

        if (addCandidateModel.getLastName() != null && !addCandidateModel.getLastName().isEmpty()){

            candidate.setLastName(addCandidateModel.getLastName());
        }

        candidate.setPosition(Position.WOMENS_COMMISSIONER);

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        candidate.setCreatedOn(currentDateTime);
        candidate.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addCandidateResponse = candidateService.addCandidate(candidate, requestId);

        log.info("[" + requestId + "] request to add Women's Commissioner candidate resulted in: " +
                addCandidateResponse);

        return ResponseEntity.status(addCandidateResponse.getStatusCode())
                .body(addCandidateResponse.getBody());
    }

    // adding voter
    @PostMapping(value = "api/v1/admin/voters/add", produces = "application/json")
    public ResponseEntity<?> addVoter (HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody AddVoterModel addVoterModel){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to add voter with username: "
                + addVoterModel.getUsername() + " to the system");

        Voter voter = new Voter();

        if (addVoterModel.getUsername() != null && !addVoterModel.getUsername().isEmpty()){

            voter.setUsername(addVoterModel.getUsername());
        }

        if (addVoterModel.getPassword() != null && !addVoterModel.getPassword().isEmpty()){

            voter.setPassword(addVoterModel.getPassword());
        }

        if (addVoterModel.getFirstName() != null && !addVoterModel.getFirstName().isEmpty()){

            voter.setFirstName(addVoterModel.getFirstName());
        }

        if (addVoterModel.getLastName() != null && !addVoterModel.getLastName().isEmpty()){

            voter.setLastName(addVoterModel.getLastName());
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        voter.setCreatedOn(currentDateTime);
        voter.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addVoterResponse = voterService.addVoter(voter, requestId);

        log.info("[" + requestId + "] request to add voter resulted in: " + addVoterResponse);

        return ResponseEntity.status(addVoterResponse.getStatusCode())
                .body(addVoterResponse.getBody());

    }

    // get candidate votes
    @GetMapping(value = "api/v1/admin/candidates/{candidateId}/getvotes", produces = "application/json")
    public ResponseEntity<?> getCandidateVotes (HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable Long candidateId){

        String requestId = request.getSession().getId();

        log.info("[" + requestId + "] about to process request to get the votes of candidate with ID: "
                + candidateId);

        Optional<Candidate> optionalCandidate = candidateService.findById(candidateId);

        if (optionalCandidate.isEmpty()){
            log.info("[" + requestId + "] request to get all votes of candidate failed. Candidate does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate does not exist");
        }

        Candidate candidate = optionalCandidate.get();
        List<Vote> candidateVotes = candidate.getVotes();

        log.info("[" + requestId + "] request to get all votes of candidate is: "
                + candidateVotes.size());

        return ResponseEntity.status(HttpStatus.OK)
                .body(candidate.getFirstName() + " " + candidate.getLastName() + " got " + candidateVotes.size() + " votes");
    }



}
