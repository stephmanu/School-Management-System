package com.steph.SchoolManagementSystem.v1.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("Admin")
    ADMIN,

    @JsonProperty("Student")
    STUDENT,

    @JsonProperty("Professor")
    PROFESSOR

}
