package com.steph.onlinecollegeelectionscentre.v1.repositories;

import com.steph.onlinecollegeelectionscentre.v1.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository<Vote, Long> {
}