package org.okten.demo.mapper;

import org.okten.demo.dto.ProductDto;
import org.okten.demo.entity.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto mapToDto(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .availability(entity.getAvailability())
                .category(entity.getCategory())
                .owner(entity.getOwner())
                .build();
    }

    public Product mapToEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAvailability(dto.getAvailability());
        product.setCategory(dto.getCategory());
        product.setOwner(dto.getOwner());
        return product;
    }

    @PreAuthorize("#target.owner == authentication.principal")
    public Product updatePartially(Product target, ProductDto updateWith) {
        if (updateWith.getName() != null) {
            target.setName(updateWith.getName());
        }

        if (updateWith.getDescription() != null) {
            target.setDescription(updateWith.getDescription());
        }

        if (updateWith.getPrice() != null) {
            target.setPrice(updateWith.getPrice());
        }

        if (updateWith.getAvailability() != null) {
            target.setAvailability(updateWith.getAvailability());
        }

        if (updateWith.getCategory() != null) {
            target.setCategory(updateWith.getCategory());
        }

        if (updateWith.getOwner() != null) {
            target.setOwner(updateWith.getOwner());
        }

        return target;
    }
}
