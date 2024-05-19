package com.steph.SchoolManagementSystem.v1.controllers;

import com.steph.SchoolManagementSystem.v1.entities.*;
import com.steph.SchoolManagementSystem.v1.models.*;
import com.steph.SchoolManagementSystem.v1.services.*;
//import com.steph.SchoolManagementSystem.v1.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrolledService enrolledService;

    public StudentController(StudentService studentService,
                             CourseService courseService,
                             EnrolledService enrolledService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrolledService = enrolledService;

        Objects.requireNonNull(studentService, "student service is required");
        Objects.requireNonNull(courseService, "course service is required");
        Objects.requireNonNull(enrolledService, "enrol service is required");
    }

    // Student enrolling into courses
    @PostMapping(value = "api/v1/{studentId}/courses/{courseId}/enrol", produces = "application/json")
    public ResponseEntity<?> enrol (HttpServletResponse response, HttpServletRequest request,
                                    @PathVariable Long studentId, @PathVariable Long courseId){

        String requestId = request.getSession().getId();
        log.info("[" +requestId + "] is about to process request to enrol into course with id " + courseId);

        Optional<Student> optionalStudent = studentService.findById(studentId);
        if(optionalStudent.isEmpty()){
            log.info("[ " + requestId + " ] request to enrol into course failed, Student cannot be found");
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student cannot be found");
        }

        Optional<Course> optionalCourse = courseService.findById(courseId);
        if(optionalCourse.isEmpty()){
            log.info("[ " + requestId + " ] request to enrol into course failed, Course cannot be found");
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot be found");

        }


        Student student = optionalStudent.get();
        Course course = optionalCourse.get();

        //verify if student has already enrolled into course
        Boolean isStudentEnrolled = enrolledService.isEnrolled(student, course, requestId);

        if (isStudentEnrolled){
            log.info("Student has already enrolled.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body("Student has already enrolled into course");
        }


        Enrolled enrol = new Enrolled();

        if (!student.toString().isEmpty()){
            enrol.setStudent(student);
        }

        if (!course.toString().isEmpty()){
            enrol.setCourse(course);
        }

        ZonedDateTime currentDate = ZonedDateTime.now();
        enrol.setEnrollmentDateTime(currentDate);

        ResponseEntity<?> addEnrollmentResponse = enrolledService.addEnrollment(enrol, requestId);

        student.getEnrollment().add(enrol);
        course.getEnrol().add(enrol);

        studentService.addEditedStudent(student, requestId);
        courseService.addEditedCourse(course, requestId);

        log.info("[ " + requestId + " ] request to enrol into course with id "
                + courseId + ", resulted in: " + addEnrollmentResponse);

        return ResponseEntity.status(addEnrollmentResponse.getStatusCode())
                .body(addEnrollmentResponse.getBody());
    }


    // Student editing their details
    @PutMapping("api/v1/{studentId}/edit")
    public ResponseEntity<?> editStudentDetails (HttpServletResponse response, HttpServletRequest request,
                                              @PathVariable Long studentId,
                                              @RequestBody EditStudentModel editStudentModel){

        String requestId = request.getSession().getId();
        log.info("[" +requestId + "] is about to process request to edit student details with id " + studentId);

        Optional<Student> optionalStudent = studentService.findById(studentId); // find student by id

        //check if optionalStudent is empty, if yes throw exception
        if(optionalStudent.isEmpty()){
            log.info("[ " + requestId + " ] request to edit Student detail failed, Student cannot be found");
           
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("student cannot be found");
        }

        Student student = optionalStudent.get();// unwrap student from optional

        //set updated details for student

        if ((editStudentModel.getEmail() != null) && !editStudentModel.getEmail().isEmpty()){
            student.setEmail(editStudentModel.getEmail());}


        if ((editStudentModel.getPassword() != null) && !editStudentModel.getPassword().isEmpty()){
            student.setPassword(editStudentModel.getPassword());}

        if ((editStudentModel.getFirstName() != null) && !editStudentModel.getFirstName().isEmpty()){
            student.setFirstName(editStudentModel.getFirstName());}

        if ((editStudentModel.getLastName() != null) && !editStudentModel.getLastName().isEmpty()){
            student.setLastName(editStudentModel.getLastName());}


        if ((editStudentModel.getDob() != null) && !editStudentModel.getDob().toString().isEmpty()){
            student.setDob(editStudentModel.getDob());}


        if ((editStudentModel.getPhoneNumber() != null) && !editStudentModel.getPhoneNumber().isEmpty()){
            student.setPhoneNumber(editStudentModel.getPhoneNumber());}

        if ((editStudentModel.getAddress() != null) && !editStudentModel.getAddress().toString().isEmpty()){
            student.setAddress(editStudentModel.getAddress());}

        ZonedDateTime currentDate = ZonedDateTime.now();
        student.setUpdatedOn(currentDate);


        ResponseEntity<?> addEditedStudentResponse = studentService.addEditedStudent(student, requestId);

        log.info("[ " + requestId + " ] request to edit student with id "
                + studentId + ", resulted in: " + addEditedStudentResponse);

        return ResponseEntity.status(addEditedStudentResponse.getStatusCode())
                .body(addEditedStudentResponse.getBody());
    }


}
