package com.steph.SchoolManagementSystem.v1.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class ProfessorResponseDto {

    private String firstName;

    private String lastName;

    private String email;
}
