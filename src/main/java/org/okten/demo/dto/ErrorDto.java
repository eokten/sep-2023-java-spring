package org.okten.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDto {

    private String details;

    private LocalDateTime timestamp;
}
