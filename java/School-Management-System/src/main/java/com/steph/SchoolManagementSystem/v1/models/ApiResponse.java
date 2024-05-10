package com.steph.SchoolManagementSystem.v1.models;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiResponse<T> extends ResponseEntity<T> {

    public ApiResponse(HttpServletRequest request, HttpStatus status, Object body, List<Error> errors) {
        super((T) new CustomBody<T>(request, status, body, errors), status);
    }

}
