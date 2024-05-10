package com.steph.SchoolManagementSystem.v1.controllers;

import com.steph.SchoolManagementSystem.v1.entities.Course;
import com.steph.SchoolManagementSystem.v1.entities.Enrolled;
import com.steph.SchoolManagementSystem.v1.entities.Professor;
import com.steph.SchoolManagementSystem.v1.entities.Student;
import com.steph.SchoolManagementSystem.v1.models.EditProfessorModel;
import com.steph.SchoolManagementSystem.v1.models.ProfessorResponseDto;
import com.steph.SchoolManagementSystem.v1.services.CourseService;
import com.steph.SchoolManagementSystem.v1.services.EnrolledService;
import com.steph.SchoolManagementSystem.v1.services.ProfessorService;
import com.steph.SchoolManagementSystem.v1.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class ProfessorController {

    private final ProfessorService professorService;

    private final CourseService courseService;

    private final StudentService studentService;

    private final EnrolledService enrolledService;

    public ProfessorController(ProfessorService professorService, CourseService courseService,
                               StudentService studentService, EnrolledService enrolledService) {
        this.professorService = professorService;
        this.courseService = courseService;
        this.enrolledService = enrolledService;
        this.studentService = studentService;

        Objects.requireNonNull(professorService, "professor service is required");
        Objects.requireNonNull(courseService, "course service is required");
        Objects.requireNonNull(enrolledService, "enrolled service is required");
        Objects.requireNonNull(studentService, "student service is required");

    }



    // professor editing their details
    @PutMapping(value = "api/v1/{professorId}/editprofessor", produces = "application/json")
    public ResponseEntity<?> editDetails (HttpServletResponse response, HttpServletRequest request,
                                          @PathVariable Long professorId,
                                          @RequestBody EditProfessorModel editProfessorModel){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to edit professor details with id " + professorId);

        Optional<Professor> optionalProfessor = professorService.findById(professorId); // find professor by id

        //check if optionalProfessor is empty, if yes throw exception
        if(optionalProfessor.isEmpty()){
            log.info("[ " + requestId + " ] request to edit professor detail failed, professor cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor cannot be found");
        }

        Professor professor = optionalProfessor.get();// unwrap professor from optional

        //set updated details for professor

        if ((editProfessorModel.getEmail() != null) && !editProfessorModel.getEmail().isEmpty()){
            professor.setEmail(editProfessorModel.getEmail());}


        if ((editProfessorModel.getPassword() != null) && !editProfessorModel.getPassword().isEmpty()){
            professor.setPassword(editProfessorModel.getPassword());
        }


        if ((editProfessorModel.getFirstName() != null) && !editProfessorModel.getFirstName().isEmpty()){
            professor.setFirstName(editProfessorModel.getFirstName());
        }


        if ((editProfessorModel.getLastName() != null) && !editProfessorModel.getLastName().isEmpty()){
            professor.setLastName(editProfessorModel.getLastName());
        }


        if ((editProfessorModel.getDob() != null) && !editProfessorModel.getDob().toString().isEmpty()){
            professor.setDob(editProfessorModel.getDob());}


        if ((editProfessorModel.getPhoneNumber() != null) && !editProfessorModel.getPhoneNumber().isEmpty()){
            professor.setPhoneNumber(editProfessorModel.getPhoneNumber());
        }

        if ((editProfessorModel.getAddress() != null) && !editProfessorModel.getAddress().toString().isEmpty()){
            professor.setAddress(editProfessorModel.getAddress());
        }

        ZonedDateTime currentDate = ZonedDateTime.now();
        professor.setUpdatedOn(currentDate);


        ResponseEntity<?> addEditedProfResponse = professorService.addEditedProfessor(professor, requestId);

        ProfessorResponseDto responseDto = null;
        responseDto = professor.toResponse();

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to edit professor with id "
                + professorId + ", resulted in: " + addEditedProfResponse);

        return ResponseEntity.status(addEditedProfResponse.getStatusCode())
                .body(addEditedProfResponse.getBody());
    }

    //edit student grade in a course
    @PutMapping("api/v1/{professorId}/{courseId}/enrol/{studentId}/grade")
    public ResponseEntity<?> editStudentGrade (HttpServletResponse response, HttpServletRequest request,
                                            @RequestParam Double grade,
                                            @PathVariable Long professorId,
                                            @PathVariable Long courseId,
                                            @PathVariable Long studentId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to edit student with ID"
                        + studentId + "'s grade in course with ID" + courseId);

        // find professor by id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        //check if optionalProfessor is empty, if yes throw exception
        if(optionalProfessor.isEmpty()){
            log.info("[ " + requestId + " ] request to edit student grade failed, professor cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor cannot be found");
        }

        Optional<Course> optionalCourse = courseService.findById(courseId); // find course by id
        if(optionalCourse.isEmpty()){
            log.info("[ " + requestId + " ] request to edit student grade failed, course cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Course cannot be found");
        }

        // find student by id
        Optional<Student> optionalStudent = studentService.findById(courseId);
        if(optionalStudent.isEmpty()){
            log.info("[ " + requestId + " ] request to edit student grade failed, student cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Student cannot be found");
        }


        Student student = optionalStudent.get(); //unwrap student from optional
        Course course = optionalCourse.get(); // unwrap course from optional
        Professor professor = optionalProfessor.get(); // unwrap course from optional

        //check if prof is eligible to edit student grade... prof is eligible only if he teaches the course
        List<Course> profCourses = professor.getCourses();

        if (!profCourses.contains(course)) {

            log.info("[ " + requestId + " ] request to edit student grade failed, prof does not teach course");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor does not teach course");
        }

        Enrolled enrollment = enrolledService.findEnrollment(studentId, courseId);

        if (enrollment != null && !enrollment.toString().isEmpty()){
            enrollment.setGrade(grade);

        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enrollment does no exist.");

        }

        ResponseEntity<?> addEditedEnrollmentResponse = enrolledService.addEnrollment(enrollment, requestId);

        courseService.addEditedCourse(course, requestId);
        professorService.addEditedProfessor(professor, requestId);
        if (enrollment != null) {
            studentService.addEditedStudent(enrollment.getStudent(), requestId);
        }

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to edit student grade resulted in: "
                + addEditedEnrollmentResponse);

        return ResponseEntity.status(addEditedEnrollmentResponse.getStatusCode())
                .body(addEditedEnrollmentResponse.getBody());
    }

}
