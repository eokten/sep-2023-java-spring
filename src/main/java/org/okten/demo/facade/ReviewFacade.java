package org.okten.demo.facade;

import lombok.RequiredArgsConstructor;
import org.example.rest.model.ProductDto;
import org.okten.demo.dto.ReviewDto;
import org.okten.demo.dto.SendMailDto;
import org.okten.demo.service.MailService;
import org.okten.demo.service.ProductService;
import org.okten.demo.service.ReviewService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

    private final ProductService productService;

    private final ReviewService reviewService;

    private final MailService mailService;

    public ReviewDto createReview(Long productId, ReviewDto reviewDto) {
        Optional<ProductDto> productOpt = productService.findProduct(productId);

        if (productOpt.isEmpty()) {
            throw new NoSuchElementException("Product with id %s not found".formatted(productId));
        }

        ReviewDto savedReview = reviewService.saveReview(productId, reviewDto);

        String subject = "New review for product %s".formatted(productOpt.get().getName());
        String text = "You have a new review with rating %s and text: '%s'".formatted(savedReview.getRating(), savedReview.getText());

//        mailService.sendMail(SendMailDto.builder()
//                .subject(subject)
//                .text(text)
//                .recipient(productOpt.get().getOwner())
//                .build());

        return savedReview;
    }

    public List<ReviewDto> getReviews(Long productId) {
        return reviewService.getReviews(productId);
    }
}
