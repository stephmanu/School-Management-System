package com.steph.SchoolManagementSystem.v1.repositories;

import com.steph.SchoolManagementSystem.v1.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsProfessorByEmail(String email);

    boolean existsProfessorByPassword(String password);
}