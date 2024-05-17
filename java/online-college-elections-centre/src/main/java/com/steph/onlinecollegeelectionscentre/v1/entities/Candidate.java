package com.steph.onlinecollegeelectionscentre.v1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.steph.onlinecollegeelectionscentre.v1.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidateId")
    private Long candidateId;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Position position;

    @JsonManagedReference
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @CreationTimestamp
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime updatedOn;
}
