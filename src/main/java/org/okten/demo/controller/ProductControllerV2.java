package org.okten.demo.controller;

import lombok.RequiredArgsConstructor;
import org.okten.demo.entity.Product;
import org.okten.demo.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class ProductControllerV2 {

    private final ProductRepository productRepository;

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.of(productRepository.findById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice) {

        List<Product> result;

        if (maxPrice != null && minPrice != null) {
            result = productRepository.findAllByPriceBetween(minPrice, maxPrice);
        } else if (maxPrice != null) {
            result = productRepository.findAllByPriceLessThanEqual(maxPrice);
        } else if (minPrice != null) {
            result = productRepository.findAllByPriceGreaterThanEqual(minPrice);
        } else {
            result = productRepository.findAll();
        }

        return ResponseEntity.ok(result);
    }
}
