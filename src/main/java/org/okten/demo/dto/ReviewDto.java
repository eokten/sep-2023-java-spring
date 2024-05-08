package org.okten.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {

    private Long productId;

    private String text;

    private Integer rating;

    private LocalDateTime timestamp;
}
