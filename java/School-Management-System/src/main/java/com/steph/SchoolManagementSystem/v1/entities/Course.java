package com.steph.SchoolManagementSystem.v1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.steph.SchoolManagementSystem.v1.models.CourseResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String code;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private Integer minStudent;

    private Integer maxStudent;

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrolled> enrol;

    @CreationTimestamp
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime updatedOn;




    public CourseResponseDto toResponse (){
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setCode(this.code);
        responseDto.setName(this.name);
        responseDto.setStartDate(this.startDate);
        responseDto.setEndDate(this.endDate);

        return responseDto;
    }

}



