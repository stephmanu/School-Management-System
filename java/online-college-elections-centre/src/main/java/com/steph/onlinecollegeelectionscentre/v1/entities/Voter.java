package com.steph.onlinecollegeelectionscentre.v1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.steph.onlinecollegeelectionscentre.v1.dtos.VoterResponseDto;
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
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voterId")
    private Long voterId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private  String password;

    private String firstName;

    private String lastName;

    @JsonManagedReference
    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL)
    private List<Vote> votes;


    @CreationTimestamp
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime updatedOn;

    public VoterResponseDto toResponseDto(){
        VoterResponseDto responseDto = new VoterResponseDto();
        responseDto.setUsername(this.username);
        responseDto.setFirstName(this.firstName);
        responseDto.setLastName(this.lastName);

        return responseDto;
    }

}
