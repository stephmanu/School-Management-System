package com.steph.SchoolManagementSystem.v1.services;

import com.steph.SchoolManagementSystem.v1.entities.Course;
import com.steph.SchoolManagementSystem.v1.entities.Enrolled;
import com.steph.SchoolManagementSystem.v1.entities.Student;
import com.steph.SchoolManagementSystem.v1.repositories.EnrolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class EnrolledService {

    private final EnrolRepository enrolRepository;
    public Enrolled findEnrollment(Long studentId, Long courseId){

        return enrolRepository.findByStudent_IdAndCourse_Id(studentId, courseId);

    }

    public EnrolledService(EnrolRepository enrolRepository) {
        this.enrolRepository = enrolRepository;
        Objects.requireNonNull(enrolRepository, "enrol repository is required");
    }


    public ResponseEntity<?> addEnrollment (Enrolled enrol, String requestId){
        log.info("[" + requestId + "] is about to process request to add enrollment");

        try {
            enrolRepository.save(enrol);
        } catch (Exception e) {
            log.warn("an error occurred while persisting enrollment. message: " + e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.OK).body(enrol);
    }


    public Boolean isEnrolled(Student student, Course course, String requestId) {
            log.info("[" + requestId + "] is about to process request to verify if student has already enrolled into course.");

        return enrolRepository.existsEnrolledByStudent_Id(student.getId()) &&
                    enrolRepository.existsEnrolledByCourse_Id(course.getId());

    }

}


