package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiErrorResponse {

    private Integer code;

    private String message;

    private LocalDateTime timestamp;
}