package org.okten.demo.controller;

import lombok.RequiredArgsConstructor;
import org.okten.demo.dto.ReviewDto;
import org.okten.demo.facade.ReviewFacade;
import org.okten.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    // Variant 1
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@RequestParam("productId") Long productId) {
        return ResponseEntity.ok(reviewFacade.getReviews(productId));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDto> getReviews(@RequestParam("productId") Long productId, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewFacade.createReview(productId, reviewDto));
    }
}
