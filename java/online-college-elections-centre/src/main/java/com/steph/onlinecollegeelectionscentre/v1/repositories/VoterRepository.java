package com.steph.onlinecollegeelectionscentre.v1.repositories;

import com.steph.onlinecollegeelectionscentre.v1.entities.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Long> {
    boolean existsVoterByUsername(String username);

    boolean existsVoterByPassword(String password);

    Optional<Voter> findByUsernameAndPassword(String username, String password);
}