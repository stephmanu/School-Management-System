package com.steph.SchoolManagementSystem.v1.repositories;

import com.steph.SchoolManagementSystem.v1.entities.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolRepository extends JpaRepository<Enrolled, Long> {
    boolean existsEnrolledByStudent_Id(Long id);

    boolean existsEnrolledByCourse_Id(Long id);

    Enrolled findByStudent_IdAndCourse_Id(Long studentId, Long courseId);
}