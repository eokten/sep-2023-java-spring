package org.okten.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.okten.demo.dto.ProductDto;
import org.okten.demo.dto.ReviewDto;
import org.okten.demo.facade.ReviewFacade;
import org.okten.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class ProductControllerV2 {

    // SOAP - Simple Object Access Protocol - XML
    // RESTful
    // GraphQL
    // GRPC (RPC) - Remote Procedure Call - protobuf, avro

    private final ProductService productService;

    private final ReviewFacade reviewFacade;

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.of(productService.findProduct(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Variant 2
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewFacade.getReviews(productId));
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewDto> getReviews(@PathVariable("productId") Long productId, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewFacade.createReview(productId, reviewDto));
    }
}
