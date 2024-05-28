package org.okten.demo.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreatedEvent {

    private Long productId;

    private String name;

    private Double price;

    private String category;
}
