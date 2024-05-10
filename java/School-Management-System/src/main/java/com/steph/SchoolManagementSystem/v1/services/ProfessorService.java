package com.steph.SchoolManagementSystem.v1.services;

import com.steph.SchoolManagementSystem.v1.entities.Course;
import com.steph.SchoolManagementSystem.v1.entities.Professor;
import com.steph.SchoolManagementSystem.v1.repositories.ProfessorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
        Objects.requireNonNull(professorRepository, "professor repository is required");
    }

    //Adjusts the professor's salary based on the number of courses they are teaching.
    //If the number of courses is greater than extraCourseCount, the professor
    //receives a bonus amount of bonusAmount.
    public Double professorSalaryPayable(Professor professor) {

        int extraCourseCount = 4;
        double bonusAmount = 20000.0;

        if (professor.getCourses() != null && professor.getCourses().size() > extraCourseCount) {
            professor.setSalary(professor.getSalary() + bonusAmount);
        }

        return professor.getSalary();
    }


    public ResponseEntity<?> addProfessor(Professor professor, String requestId) {
        log.info("[" + requestId + "] is about to process request to add professor with email: "
                + professor.getEmail());

        if (professorRepository.existsProfessorByEmail(professor.getEmail()) ||
                professorRepository.existsProfessorByPassword(professor.getPassword())){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password already exist");

        }

        try {
            professorRepository.save(professor);

        } catch (Exception e) {
            log.warn("an error occurred while persisting professor. message: " + e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    public Optional<Professor> findById(Long professorId) {
        return professorRepository.findById(professorId);
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public ResponseEntity<?> addEditedProfessor(Professor professor, String requestId) {
        log.info("[" + requestId + "] is about to process request to add edited professor to repository");

        try {
            professorRepository.save(professor);
        } catch (Exception e) {
            log.warn("an error occurred while persisting professor. message: " + e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.OK).body(professor);

    }
}
