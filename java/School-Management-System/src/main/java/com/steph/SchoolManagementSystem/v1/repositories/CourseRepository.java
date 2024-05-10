package com.steph.SchoolManagementSystem.v1.repositories;

import com.steph.SchoolManagementSystem.v1.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsCourseByCode(String code);

    boolean existsCourseByName(String name);
}