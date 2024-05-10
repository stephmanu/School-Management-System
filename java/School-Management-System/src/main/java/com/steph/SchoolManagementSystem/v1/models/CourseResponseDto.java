package com.steph.SchoolManagementSystem.v1.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonSerialize
public class CourseResponseDto {

    private String code;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
