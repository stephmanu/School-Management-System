package com.steph.SchoolManagementSystem.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.steph.SchoolManagementSystem.v1.entities.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    //boolean existsStudentByEmailAndPassword(String email, String password);

    boolean existsStudentByEmail(String email);

    boolean existsStudentByPassword(String password);
}
