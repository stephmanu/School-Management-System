package com.steph.onlinecollegeelectionscentre.v1.repositories;

import com.steph.onlinecollegeelectionscentre.v1.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}