package org.okten.demo.mapper;

import org.example.rest.model.ProductDto;
import org.okten.demo.entity.Product;
import org.okten.demo.entity.ProductAvailability;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto mapToDto(Product entity) {
        return new ProductDto()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .status(mapAvailability(entity.getAvailability()));
    }

    public ProductDto.StatusEnum mapAvailability(ProductAvailability availability) {
        if (availability == null) {
            return ProductDto.StatusEnum.OUT_OF_STOCK;
        }
        return switch (availability) {
            case AVAILABLE -> ProductDto.StatusEnum.IN_STOCK;
            case DISCONTINUED -> ProductDto.StatusEnum.DISCONTINUED;
            default -> ProductDto.StatusEnum.OUT_OF_STOCK;
        };
    }

    public Product mapToEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        return product;
    }

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

        if (updateWith.getCategory() != null) {
            target.setCategory(updateWith.getCategory());
        }

        if (updateWith.getQuantity() != null) {
            if (updateWith.getQuantity() > 0) {
                target.setAvailability(ProductAvailability.AVAILABLE);
            } else if (updateWith.getQuantity() == 0) {
                target.setAvailability(ProductAvailability.OUT_OF_STOCK);
            } else {
                target.setAvailability(ProductAvailability.NOT_AVAILABLE);
            }
        }

        return target;
    }
}
