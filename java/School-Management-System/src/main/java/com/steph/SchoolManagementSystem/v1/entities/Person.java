package com.steph.SchoolManagementSystem.v1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.steph.SchoolManagementSystem.v1.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@JsonSerialize
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = -1596321L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String password;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    @JsonIgnore
    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @CreatedDate
    @CreationTimestamp
    private ZonedDateTime createdOn;

    @LastModifiedDate
    @UpdateTimestamp
    private ZonedDateTime updatedOn;

}



