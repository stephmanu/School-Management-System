package com.steph.SchoolManagementSystem.v1.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.steph.SchoolManagementSystem.v1.entities.Address;
import lombok.Data;

@Data
@JsonSerialize
public class StudentResponseDto {

    private String firstName;

    private String lastName;

    private String email;
}
