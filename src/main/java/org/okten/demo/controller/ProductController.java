package org.okten.demo.controller;

import lombok.RequiredArgsConstructor;
import org.example.rest.controller.ProductsApi;
import org.example.rest.model.ProductDto;
import org.okten.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long id) {
        return ResponseEntity.of(productService.findProduct(id));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProducts(String category) {
        return ResponseEntity.ok(productService.getProducts(category));
    }

    @Override
    public ResponseEntity<ProductDto> modifyProduct(Long id, ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @Override
    public ResponseEntity<ProductDto> modifyProductPartially(Long id, ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }
}
