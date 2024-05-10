package com.steph.SchoolManagementSystem.v1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.steph.SchoolManagementSystem.v1.models.ProfessorResponseDto;
import com.steph.SchoolManagementSystem.v1.models.StudentResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Professor extends Person{

    private Double salary;

    @JsonManagedReference
    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    private List<Course> courses;



    public ProfessorResponseDto toResponse (){
        ProfessorResponseDto responseDto = new ProfessorResponseDto();
        responseDto.setEmail(this.getEmail());
        responseDto.setFirstName(this.getFirstName());
        responseDto.setLastName(this.getLastName());

        return responseDto;
    }

}
