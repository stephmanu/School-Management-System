package com.steph.SchoolManagementSystem.v1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.steph.SchoolManagementSystem.v1.models.StudentResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Person {

    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrolled> enrollment;

    private Boolean isInternational;


    public StudentResponseDto toResponse (){
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setEmail(this.getEmail());
        responseDto.setFirstName(this.getFirstName());
        responseDto.setLastName(this.getLastName());

        return responseDto;
    }
}
