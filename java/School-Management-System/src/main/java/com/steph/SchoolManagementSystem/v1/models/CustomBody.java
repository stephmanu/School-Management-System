package com.steph.SchoolManagementSystem.v1.models;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

@ResponseBody
public class CustomBody<T> implements Serializable {
    private String requestId;
    private int status;
    private String message;
    private Object data;
    private @Setter
    List<Error> errors;
    private long totalCount;
    private int page;
    private int pageSize;


    public CustomBody(Object request, HttpStatus status, Object data, List<Error> errors) {
        if (request != null) {
            if (request instanceof HttpServletRequest) {
                Object requestId = ((HttpServletRequest) request).getAttribute("requestId");
                this.requestId = StringUtils.isEmpty(requestId) ? null : (String) requestId;
            } else {
                this.requestId = (String) request;
            }
        }

        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
        this.errors = errors;
        this.page = 1;
        this.totalCount = 0;
        this.pageSize = 0;
    }


}
