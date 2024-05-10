package com.steph.SchoolManagementSystem.v1.controllers;

import com.steph.SchoolManagementSystem.v1.entities.Student;
import com.steph.SchoolManagementSystem.v1.entities.Course;
import com.steph.SchoolManagementSystem.v1.entities.Professor;
import com.steph.SchoolManagementSystem.v1.enums.Role;
import com.steph.SchoolManagementSystem.v1.models.*;
import com.steph.SchoolManagementSystem.v1.services.CourseService;
import com.steph.SchoolManagementSystem.v1.services.ProfessorService;
import com.steph.SchoolManagementSystem.v1.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
public class AdminController {

    private final StudentService studentService;

    private final CourseService courseService;

    private final ProfessorService professorService;

    public AdminController(Student student, StudentService studentService,
                           CourseService courseService, ProfessorService professorService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.professorService = professorService;

        Objects.requireNonNull(professorService, "professor service is required");
        Objects.requireNonNull(studentService, "student service is required");
        Objects.requireNonNull(courseService, "course service is required");
    }

    // Adding student to repository
    @PostMapping(value = "api/v1/admin/students/add", produces = "application/json")
    public ResponseEntity<?> addStudent (HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody AddStudentModel addStudentModel){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to add student");

        //create an instance of student and set the attributes
        Student student = new Student();
        student.setRole(Role.STUDENT);
        student.setEmail(addStudentModel.getEmail());
        student.setPassword(addStudentModel.getPassword());
        student.setIsInternational(addStudentModel.getIsInternational());

        ZonedDateTime currentDate = ZonedDateTime.now();
        student.setCreatedOn(currentDate);
        student.setUpdatedOn(currentDate);

        // add student to repository
        ResponseEntity<?> addStudentResponse = studentService.addStudent(student, requestId);

        response.setStatus(status);
        log.info(String.valueOf(addStudentResponse));
        return (ResponseEntity<?>)
                ResponseEntity.status(addStudentResponse.getStatusCode())
                .contentType(MediaType.valueOf("application/json"))
                .body(addStudentResponse.getBody());

    }

    // get all students
    @GetMapping(value = "api/v1/admin/students/getall", produces = "application/json")
    public ResponseEntity<?> getAllStudents (HttpServletResponse response, HttpServletRequest request,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "isPaged", required = false, defaultValue = "false") Boolean isPaged,
                                          @RequestParam(value = "sortBy", required = false, defaultValue = "createdOn") String sortBy,
                                          @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get all students");

        // setting up sorting and pagination
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page,pageSize,sort);

        //get all students from repo
        List<Student> studentList = studentService.findAll();

        //check if student list is empty, if yes throw error
        if(studentList.isEmpty()){

            log.info("[ " + requestId + " ] request to get all students failed, no student available.");

            status = 404;
            response.setStatus(status);
            return new ResponseEntity<String>("No student available.",HttpStatus.BAD_REQUEST);

        }

        //create a list of students with studentResponseDto type
        List<StudentResponseDto> students = new ArrayList<>();

        //add students from studentList to students list with responseDto type
        studentList.forEach(student -> students.add(student.toResponse()));

        //create a page with students list, size and pagination details
        Page<StudentResponseDto> pagedStudents = new PageImpl<>(students,pageable,students.size());

        response.setStatus(status);

        log.info("[ " + requestId + " ] request to get all students is successful");

        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body (isPaged ? pagedStudents : students);

    }

    // Get one student
    @GetMapping(value = "api/v1/admin/students/{studentId}", produces = "application/json")
    public ResponseEntity<?> getStudent (HttpServletResponse response, HttpServletRequest request,
                                      @PathVariable Long studentId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get student with ID " + studentId);

        //get student from repo using student id provided
        Optional<Student> optionalStudent = studentService.findById(studentId);

        //check if student is empty, if yes throw error
        if (optionalStudent.isEmpty()){
            log.info("[ " + requestId + " ] request to get student failed, student cannot be found");
            status = 400;
            response.setStatus(status);

            return new ResponseEntity<String> ("Student can not be found", HttpStatus.BAD_REQUEST);

        }

        //unwrap student from optional
        Student student = optionalStudent.get();

        /*StudentResponseDto responseDto = null;
        responseDto = student.toResponse();
*/
        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get student with ID " + studentId + " is successful");
        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body(student);

    }

    // is student part-time or full-time
    @GetMapping(value = "api/v1/admin/students/{studentId}/enrollmentstatus", produces = "application/json")
    public ResponseEntity<?> studentEnrollmentStatus (HttpServletResponse response, HttpServletRequest request,
                                                      @PathVariable Long studentId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to check student's enrollment status");

        // find student from repo using student id
        Optional<Student> optionalStudent = studentService.findById(studentId);

        //verify if student exist, if not throw error
        if (optionalStudent.isEmpty()){
            log.info("[ " + requestId + " ] request to get student failed, student cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("Student cannot be found"));
            return new ResponseEntity<String> ("Student can not be found", HttpStatus.BAD_REQUEST);
        }

        // unwrap student from optional
        Student student = optionalStudent.get();

        Boolean enrollmentStatus = studentService.isPartTime(student);

        log.info("Is student with ID:" + studentId + " part-time? - " + enrollmentStatus);

        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body("Is student with ID:" + studentId + " part-time? - " + enrollmentStatus);

    }

    // is student on probation
    @GetMapping(value = "api/v1/admin/students/{studentId}/onprobation", produces = "application/json")
    public ResponseEntity<?> isStudentOnProbation (HttpServletResponse response, HttpServletRequest request,
                                                      @PathVariable Long studentId) {

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" + requestId + "] is about to process request to check if student is on probation");

        // find student from repo using student id
        Optional<Student> optionalStudent = studentService.findById(studentId);

        //verify if student exist, if not throw error
        if (optionalStudent.isEmpty()) {
            log.info("[ " + requestId + " ] request to get student failed, student cannot be found");
            status = 400;
            response.setStatus(status);

            return new ResponseEntity<String>("Student can not be found", HttpStatus.BAD_REQUEST);
        }

        // unwrap student from optional
        Student student = optionalStudent.get();

        Boolean isOnProbation = studentService.isOnProbation(student);

        log.info("Is student with ID: " + studentId + " on probation? - " + isOnProbation);

        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body("Is student with ID: " + studentId + " on probation? - " + isOnProbation);
    }


        // Adding a course to the database
    @PostMapping(value = "api/v1/admin/courses/add", produces = "application/json")
    public ResponseEntity<?> addCourse (HttpServletRequest request, HttpServletResponse response,
                                     @RequestBody AddCourseModel addCourseModel){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to add a course");

        // create a new instance of the course
        Course course = new Course();

        // set course name
        course.setName(addCourseModel.getName());

        // set course code
        course.setCode(addCourseModel.getCode());

        // set course maximum student number
        course.setMaxStudent(addCourseModel.getMaxStudent());

        // set course minimum student number
        course.setMinStudent(addCourseModel.getMinStudent());

        // set course start date
        course.setStartDate(addCourseModel.getStartDate());

        // set course end date
        course.setEndDate(addCourseModel.getEndDate());

        // add course to repository
        ResponseEntity<?> addCourseResponse = courseService.addCourse(course, requestId);

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to add a course with code " + addCourseModel.getCode()
                + ", resulted in: " + addCourseResponse);

        return (ResponseEntity<?>) ResponseEntity.status(addCourseResponse.getStatusCode())
                .contentType(MediaType.valueOf("application/json"))
                .body(addCourseResponse.getBody());
    }


    // Edit any course attribute or detail
    @PutMapping(value = "api/v1/admin/courses/{courseId}/edit", produces = "application/json")
    public ResponseEntity<?> editCourseDetail (HttpServletResponse response, HttpServletRequest request,
                                            @PathVariable Long courseId,
                                            @RequestBody AddCourseModel addCourseModel){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to edit course with id " + courseId);

        // find course by id
        Optional<Course> optionalCourse = courseService.findById(courseId);

        //check if course is empty, if yes give feedback
        if(optionalCourse.isEmpty()){

            log.info("[ " + requestId + " ] request to edit course detail failed, course cannot be found");

            status = 400;

            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot be found");

        }

        // unwrap course from optional
        Course course = optionalCourse.get();

        // set course details
        if ((addCourseModel.getCode() != null) && !addCourseModel.getCode().isEmpty()){
            course.setCode(addCourseModel.getCode());}


        if ((addCourseModel.getName() != null) && !addCourseModel.getName().isEmpty()){
            course.setName(addCourseModel.getName());}



        if ((addCourseModel.getMinStudent() != null) && !addCourseModel.getMinStudent().toString().isEmpty()){
            course.setMinStudent(addCourseModel.getMinStudent());}


        if ((addCourseModel.getMaxStudent() != null) && !addCourseModel.getMaxStudent().toString().isEmpty()){
            course.setMaxStudent(addCourseModel.getMaxStudent());}


        if (addCourseModel.getStartDate() !=null && !addCourseModel.getStartDate().toString().isEmpty()){
            course.setStartDate(addCourseModel.getStartDate());}


        if (addCourseModel.getEndDate() !=null && !addCourseModel.getEndDate().toString().isEmpty()){
            course.setEndDate(addCourseModel.getEndDate());}


        // add updated course to repository
        ResponseEntity<?> addCourseResponse = courseService.addEditedCourse(course, requestId);

        response.setStatus(status);

        log.info("[ " + requestId + " ] request to edit course with ID: " + courseId + ", resulted in:- " + addCourseResponse);

        return ResponseEntity.status(addCourseResponse.getStatusCode())
                .contentType(MediaType.valueOf("application/json"))
                .body(addCourseResponse.getBody());

    }


    // Get all courses in database
    @GetMapping(value = "api/v1/admin/courses/getall", produces = "application/json")
    public ResponseEntity<?> getAllCourses (HttpServletResponse response, HttpServletRequest request,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "isPaged", required = false, defaultValue = "false") Boolean isPaged,
                                         @RequestParam(value = "sortBy", required = false, defaultValue = "createdOn") String sortBy,
                                         @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get all courses");

        // set sorting and pagination
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page,pageSize,sort);

        //get all courses in repo
        List<Course> courseList = courseService.findAll();

        //check if courseList, if yes throw error
        if(courseList.isEmpty()){
            List<Error> errors =
                    Collections.singletonList(new Error("No courses available"));

            log.info("[ " + requestId + " ] request to get all courses failed, no courses available.");

            status = 404;
            response.setStatus(status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.valueOf("application/json")).body("No course available");
        }

        // create a new list of courses with type as course response dto
        List<CourseResponseDto> courses = new ArrayList<>();

        //add courses from courseList to new list created
        courseList.forEach(course -> courses.add(course.toResponse()));

        //set pagination
        Page<CourseResponseDto> pagedCourses = new PageImpl<>(courses,pageable,courses.size());

        response.setStatus(status);

        log.info("[ " + requestId + " ] request to get all courses is successful");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body(isPaged ? pagedCourses : courses);

    }

    // Get one course
    @GetMapping(value = "api/v1/admin/courses/{courseId}", produces = "application/json")
    public ResponseEntity<?> getCourse (HttpServletResponse response, HttpServletRequest request,
                                      @PathVariable Long courseId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get course with ID " + courseId);

        //get course from repo using id
        Optional<Course> optionalCourse = courseService.findById(courseId);

        //check if course is empty, if yes throw error
        if (optionalCourse.isEmpty()){

            log.info("[ " + requestId + " ] request to get course failed, course cannot be found");

            status = 400;

            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot be found");
        }

        //unwrap course from optional
        Course course = optionalCourse.get();

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get course with ID: " + courseId + " is successful");
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    //is course cancelled
    @GetMapping(value = "api/v1/admin/courses/{courseId}/iscancelled", produces = "application/json")
    public ResponseEntity<?> isCourseCancelled (HttpServletResponse response, HttpServletRequest request,
                                                @PathVariable Long courseId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to check if course with ID: "
                + courseId + " is cancelled.");

        // get course from repo
        Optional<Course> optionalCourse = courseService.findById(courseId);

        //check if course is empty, if yes give feedback
        if (optionalCourse.isEmpty()){

            log.info("[ " + requestId + " ] request to get course failed, course cannot be found");

            status = 400;

            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot be found");
        }

        //unwrap course from optional

        Course course = optionalCourse.get();

        Boolean isCancelled = courseService.isCancelled(course);

        log.info("Is course with ID: " + courseId + " cancelled? - " + isCancelled);

        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/json"))
                .body("Is course with ID: " + courseId + " cancelled? - " + isCancelled);

    }

    //Add a professor
    @PostMapping(value = "api/v1/admin/professors/add", produces = "application/json")
    public ResponseEntity<?> addProfessor (HttpServletResponse response, HttpServletRequest request,
                                        @RequestBody AddProfessorModel addProfessorModel){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to add professor");

        //create an instance of professor and set the attributes
        Professor professor = new Professor();

        professor.setRole(Role.PROFESSOR);
        professor.setEmail(addProfessorModel.getEmail());
        professor.setPassword(addProfessorModel.getPassword());

        ZonedDateTime currentDate = ZonedDateTime.now();
        professor.setCreatedOn(currentDate);
        professor.setUpdatedOn(currentDate);

        // add professor to repository
        ResponseEntity<?> addProfessorResponse = professorService.addProfessor(professor, requestId);

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to add professor resulted in: " + addProfessorResponse);

        return ResponseEntity.status(addProfessorResponse.getStatusCode())
                .body(addProfessorResponse.getBody());
    }

    //Edit professor salary
    @PutMapping(value = "api/v1/admin/professors/{professorId}/editsalary", produces = "application/json")
    public ResponseEntity<?> editProfDetails (HttpServletResponse response, HttpServletRequest request,
                                           @PathVariable Long professorId,
                                           @RequestParam(required = false) Double salary){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to edit professor's salary");

        // find professor by id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        //check if professor is empty, if yes throw exception
        if(optionalProfessor.isEmpty()){
            log.info("[ " + requestId + " ] request to edit professor salary failed, professor cannot be found");
            status = 400;

            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Professor cannot be found.");
        }

        // unwrap professor from optional
        Professor professor = optionalProfessor.get();

        // set professor salary
        if ((salary != null) && !salary.toString().isEmpty()){
            professor.setSalary(salary);
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();
        professor.setUpdatedOn(currentDateTime);

        // add updated prof to repository
        ResponseEntity<?> addProfessorResponse = professorService.addEditedProfessor(professor, requestId);

        ProfessorResponseDto responseDto = null;
        responseDto = professor.toResponse();

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to edit professor with id "
                + professorId + "'s salary resulted in: " + addProfessorResponse);

        return ResponseEntity.status(addProfessorResponse.getStatusCode())
                .body(addProfessorResponse.getBody());

    }


    // Add courses prof will teach
    // in adding courses to prof you select the course using id from course repo and add to prof's courses list
    @PostMapping(value = "api/v1/admin/{professorId}/addcourse", produces = "application/json")
    public ResponseEntity<?> ProfessorCourses (HttpServletResponse response, HttpServletRequest request,
                                           @PathVariable Long professorId,
                                           @RequestParam Long courseId) {

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" + requestId + "] is about to process request to assign course to professor with id " + professorId);

        // find professor by id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        //check if professor is empty, if yes throw exception
        if (optionalProfessor.isEmpty()) {
            log.info("[ " + requestId + " ] request to assign course to professor failed, professor cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Professor cannot be found.");

        }

        // unwrap professor from optional
        Professor professor = optionalProfessor.get();

        // find course by id
        Optional<Course> optionalCourse = courseService.findById(courseId);

        //check if course is empty, if yes throw exception
        if (optionalCourse.isEmpty()) {
            log.info("[ " + requestId + " ] request to assign course to professor failed, course cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course cannot be found.");

        }

        // unwrap course from optional
        Course course = optionalCourse.get();

        //add course to professor's list of courses
        professor.getCourses().add(course);

        //add prof as course tutor
        course.setProfessor(professor);

        ZonedDateTime currentDateTime = ZonedDateTime.now();
        professor.setUpdatedOn(currentDateTime);

        // add updated prof to repository
        ResponseEntity<?> addProfessorResponse = professorService.addEditedProfessor(professor, requestId);

        //add updated course to repo
        ResponseEntity<?> addCourseResponse = courseService.addEditedCourse(course, requestId);

        response.setStatus(status);

        log.info("[ " + requestId + " ] request to assign course to professor with id " + professorId
                + " resulted in: " + addProfessorResponse);

        return ResponseEntity.status(addProfessorResponse.getStatusCode())
                .body(addProfessorResponse.getBody());

    }



        // Get one professor
    @GetMapping(value = "api/v1/admin/professors/{professorId}", produces = "application/json")
    public ResponseEntity<?> getProfessor (HttpServletResponse response, HttpServletRequest request,
                                     @PathVariable Long professorId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get professor with ID: "
                + professorId);

        //get prof using id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        // check if prof is empty, if yes throw error
        if (optionalProfessor.isEmpty()){
            log.info("[ " + requestId + " ] request to get professor failed, professor cannot be found");
            status = 400;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor cannot be found");
        }

        //unwrap prof from optional
        Professor professor = optionalProfessor.get();

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get professor with ID "
                + professorId + " is successful");

        return ResponseEntity.status(HttpStatus.OK)
                .body(professor);
    }


    // Get all professors in database
    @GetMapping(value = "api/v1/admin/professor/getall", produces = "application/json")
    public ResponseEntity<?> getAllProfessors (HttpServletResponse response, HttpServletRequest request,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "isPaged", required = false, defaultValue = "false") Boolean isPaged,
                                         @RequestParam(value = "sortBy", required = false, defaultValue = "createdOn") String sortBy,
                                         @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get all professors");

        // setting sorting and pagination
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page,pageSize,sort);

        //get all prof from repo
        List<Professor> professorList = professorService.findAll();

        // check prof list, if empty throw error
        if(professorList.isEmpty()){
            List<Error> errors =
                    Collections.singletonList(new Error("No professors available"));

            log.info("[ " + requestId + " ] request to get all professors failed, no professor available.");

            status = 404;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No professor available");
        }

        //create a new list of prof of type prof response dto
        List<ProfessorResponseDto> professors = new ArrayList<>();

        // add prof from prof list to new list of type response dto
        professorList.forEach(professor -> professors.add(professor.toResponse()));

        //setting pagination
        Page<ProfessorResponseDto> pagedCourses = new PageImpl<>(professors,pageable,professors.size());

        response.setStatus(status);

        return ResponseEntity.status(HttpStatus.OK)
                .body(isPaged ? pagedCourses : professors);
    }


    //endpoint that returns the list of courses of a particular professor
    @GetMapping(value = "api/v1/admin/professors/{professorId}/courses", produces = "application/json")
    public ResponseEntity<?> getProfCourses (HttpServletResponse response, HttpServletRequest request,
                                   @PathVariable Long professorId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get all courses assigned to professor with ID: "
                + professorId);

        //get prof by id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        // check if prof is empty, if yes throw error
        if (optionalProfessor.isEmpty()){

            log.info("[ " + requestId + " ] request to get all courses assigned to professor failed, professor cannot be found.");

            status = 404;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor cannot be found.");
        }

        //unwrap prof from optional
        Professor professor = optionalProfessor.get();

        //get all courses assigned to prof
        List<Course> professorCourses = professor.getCourses();

        //check if prof courses are empty, if yes throw error
        if(professorCourses.isEmpty()){

            log.info("[ " + requestId + " ] request to get all courses assigned to professor failed, no course has been assigned to professor");

            status = 404;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No course has been assigned to professor");
        }

        //create a new list of courses of type course response dto
        List<CourseResponseDto> courses = new ArrayList<>();

        // add prof courses from professorCourses to new list of courses of type response dto
        professorCourses.forEach(course -> courses.add(course.toResponse()));

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get courses assigned to professor with ID: "
                + professorId + " is successful");

        return ResponseEntity.status(HttpStatus.OK)
                .body(courses);
    }

    // get professor salary payable at the end of the academic session
    @GetMapping("api/v1/admin/professors/{professorId}/getsalarypayable")
    public ResponseEntity<?> professorSalaryPayable(HttpServletResponse response,
                                                    HttpServletRequest request,
                                                    @PathVariable Long professorId){

        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get salary payable of professor with ID: "
                + professorId);

        //get prof by id
        Optional<Professor> optionalProfessor = professorService.findById(professorId);

        // check if prof is empty, if yes throw error
        if (optionalProfessor.isEmpty()){

            log.info("[ " + requestId + " ] request to get all courses assigned to professor failed, professor cannot be found.");

            status = 404;
            response.setStatus(status);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Professor cannot be found.");
        }

        //unwrap prof from optional
        Professor professor = optionalProfessor.get();

        Double salaryPayable = professorService.professorSalaryPayable(professor);

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get salary payable of professor with ID: "
                + professorId + " result in: " + salaryPayable);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Professor's salary payable is = " + salaryPayable);
    }


}
