package com.steph.SchoolManagementSystem.v1.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.steph.SchoolManagementSystem.v1.entities.Address;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonSerialize
public class EditProfessorModel {
    String email;
    String password;
    String firstName;
    String lastName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dob;
    String phoneNumber;
    Address address;
}
