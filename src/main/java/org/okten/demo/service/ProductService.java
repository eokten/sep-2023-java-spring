package org.okten.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.okten.demo.dto.ProductDto;
import org.okten.demo.entity.Product;
import org.okten.demo.mapper.ProductMapper;
import org.okten.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product savedProduct = productRepository.save(productMapper.mapToEntity(productDto));
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
        productRepository.deleteById(id);
    }

    public List<ProductDto> getProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }
}
