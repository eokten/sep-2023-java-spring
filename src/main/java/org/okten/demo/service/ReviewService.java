package org.okten.demo.service;

import lombok.RequiredArgsConstructor;
import org.okten.demo.dto.ReviewDto;
import org.okten.demo.entity.Review;
import org.okten.demo.mapper.ReviewMapper;
import org.okten.demo.repository.ProductRepository;
import org.okten.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    public ReviewDto saveReview(Long productId, ReviewDto reviewDto) {
        Review review = reviewMapper.mapToEntity(reviewDto);
        review.setProductId(productId);
        review.setTimestamp(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.mapToDto(savedReview);
    }

    public List<ReviewDto> getReviews(Long productId) {
        return reviewRepository
                .findAllByProductId(productId)
                .stream()
                .map(reviewMapper::mapToDto)
                .toList();
    }
}
