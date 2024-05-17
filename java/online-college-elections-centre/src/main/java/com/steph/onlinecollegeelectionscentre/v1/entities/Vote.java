package com.steph.onlinecollegeelectionscentre.v1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "voter_id", referencedColumnName = "voterId")
    private Voter voter;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidateId")
    private Candidate candidate;

    @CreationTimestamp
    private ZonedDateTime createdOn;
}
