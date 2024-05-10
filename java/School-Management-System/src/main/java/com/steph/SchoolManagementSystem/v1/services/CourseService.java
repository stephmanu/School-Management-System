package com.steph.SchoolManagementSystem.v1.services;

import com.steph.SchoolManagementSystem.v1.entities.Course;
import com.steph.SchoolManagementSystem.v1.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        Objects.requireNonNull(courseRepository, "course repository is required");
    }


    public Boolean isCancelled(Course course){
        return course.getEnrol().size() < course.getMinStudent();
    }

    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public ResponseEntity<?> addCourse(Course course, String requestId) {
            log.info("[" + requestId + "] is about to process request to add course with code "
                    + course.getCode() + " to repository");

        if (courseRepository.existsCourseByCode(course.getCode()) ||
                courseRepository.existsCourseByName(course.getName())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course (name or code) already exist");

        }

        try {
                courseRepository.save(course);
            } catch (Exception e) {
                log.warn("an error occurred while persisting course. message: " + e.getMessage());

            }
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }


    public ResponseEntity<?> addEditedCourse(Course course, String requestId) {
        log.info("[" + requestId + "] is about to process request to add edited course to repository");

        try {
            courseRepository.save(course);
        } catch (Exception e) {
            log.warn("an error occurred while persisting course. message: " + e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }


    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}

