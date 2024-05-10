package com.steph.SchoolManagementSystem.v1.services;

import com.steph.SchoolManagementSystem.v1.entities.Enrolled;
import com.steph.SchoolManagementSystem.v1.entities.Student;
import com.steph.SchoolManagementSystem.v1.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        Objects.requireNonNull(studentRepository, "student repository is required");
    }

    public Boolean isPartTime(Student student) {
        return student.getEnrollment().size() <= 2;
    }

    public Boolean isOnProbation(Student student) {

        if (student.getEnrollment() == null || student.getEnrollment().isEmpty()) {
            return false;  // No enrolments, not on probation
        }

        double totalGrade = 0.0;

        for (Enrolled enrolment : student.getEnrollment()) {
            totalGrade += enrolment.getGrade();
        }

        double averageGrade = totalGrade / student.getEnrollment().size();

        return averageGrade < 60;
    }


    public ResponseEntity<?> addEditedStudent (Student student, String requestId){
        log.info("[" + requestId + "] is about to process request to add to the repository edited student with email: "
                + student.getEmail());

        try {

            studentRepository.save(student);

        } catch (Exception e) {

            log.error("an error occurred while persisting student. message: " + e.getMessage());

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(student);

    }

    public ResponseEntity<?> addStudent (Student student, String requestId){
        log.info("[" + requestId + "] is about to process request to add to the repository student with email: " + student.getEmail());

        if (studentRepository.existsStudentByEmail(student.getEmail()) ||
                studentRepository.existsStudentByPassword(student.getPassword())){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or password already exist");

        }

        try {

                studentRepository.save(student);

        } catch (Exception e) {

            log.error("an error occurred while persisting student. message: " + e.getMessage());

            }

        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }


    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
