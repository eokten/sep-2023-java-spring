package org.okten.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.okten.demo.entity.ProductAvailability;
import org.okten.demo.validation.ValidCategory;

@Data
@Builder
public class ProductDto {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Max(value = 1000, message = "price must not be greater than 1000")
    @Min(value = 10, message = "price must not be lower than 10")
    private Double price;

    private ProductAvailability availability;

    @ValidCategory
    private String category;

    @Email
    private String owner;
}
