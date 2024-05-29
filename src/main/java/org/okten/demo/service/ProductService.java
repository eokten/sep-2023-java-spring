package org.okten.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.event.api.IDefaultServiceEventsProducer;
import org.example.event.model.ProductCreatedPayload;
import org.example.rest.model.ProductDto;
import org.okten.demo.dto.event.ProductCreatedEvent;
import org.okten.demo.entity.Product;
import org.okten.demo.mapper.ProductMapper;
import org.okten.demo.repository.ProductRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final IDefaultServiceEventsProducer eventsProducer;

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product savedProduct = productRepository.save(productMapper.mapToEntity(productDto));
        eventsProducer.sendProductCreated(new ProductCreatedPayload()
                .withProductId(savedProduct.getId().intValue())
                .withName(savedProduct.getName())
                .withCategory(savedProduct.getCategory())
                .withPrice(savedProduct.getPrice()));
        return productMapper.mapToDto(savedProduct);
    }

    public Optional<ProductDto> findProduct(Long id) {
        return productRepository
                .findById(id)
                .map(productMapper::mapToDto);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto dtoUpdateWith) {
        return productRepository
                .findById(id)
                .map(product -> productMapper.updatePartially(product, dtoUpdateWith))
                .map(productMapper::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Product with id '%s' not found".formatted(id)));
    }

    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String productOwner = product.get().getOwner();
        String username = authentication.getPrincipal().toString();

        if (!StringUtils.equalsIgnoreCase(productOwner, username)) {
            throw new AccessDeniedException("Your are not allowed to delete this product");
        }

        productRepository.deleteById(id);
    }

    public List<ProductDto> getProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }

    public List<ProductDto> getProducts(String category) {
        return productRepository
                .findAllByCategory(category)
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }
}
