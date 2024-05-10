package com.steph.SchoolManagementSystem.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddStudentModel {

    private String email;

    private String password;

    private Boolean isInternational;
}
