package org.okten.demo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.okten.demo.dto.ProductDto;
import org.okten.demo.dto.ReviewDto;
import org.okten.demo.dto.SendMailDto;
import org.okten.demo.service.MailService;
import org.okten.demo.service.ProductService;
import org.okten.demo.service.ReviewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendProductStats {

    private final ProductService productService;

    private final ReviewService reviewService;

    private final MailService mailService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void sendStats() {
        log.info("Sending product stats...");

        Map<String, List<Pair<ProductDto, Double>>> ownerWithAverageRatings = productService
                .getProducts()
                .stream()
                .flatMap(product -> reviewService
                        .getReviews(product.getId())
                        .stream()
                        .filter(review -> review.getTimestamp() != null)
                        .filter(review -> review.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1)))
                        .mapToInt(ReviewDto::getRating)
                        .average()
                        .stream()
                        .mapToObj(averageRating -> Pair.of(product, averageRating))
                )
                .collect(groupingBy(pair -> pair.getKey().getOwner()));

        ownerWithAverageRatings.forEach((owner, ratings) -> {
            double totalAverageRating = ratings
                    .stream()
                    .mapToDouble(Pair::getValue)
                    .average()
                    .orElse(0);

            mailService.sendMail(SendMailDto.builder()
                    .subject("Products total rating for last 1 minute")
                    .text("You average rating is %s".formatted(totalAverageRating))
                    .recipient(owner)
                    .build());
        });
    }

    private void sendStatsAlternativeWithoutStreamAPI() {
        List<ProductDto> products = productService.getProducts();

        Map<String, List<Double>> ownersWithRatings = new HashMap<>();

        for (ProductDto product : products) {

            List<ReviewDto> reviews = reviewService.getReviews(product.getId());

            double sum = 0;
            long count = 0;

            for (ReviewDto review : reviews) {
                if (review.getTimestamp() != null && review.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1))) {
                    sum += review.getRating();
                    count += 1;
                }
            }

            if (count > 0) {
                double averageRating = sum / count;

                if (ownersWithRatings.containsKey(product.getOwner())) {
                    ownersWithRatings.get(product.getOwner()).add(averageRating);
                } else {
                    List<Double> ratings = new LinkedList<>();
                    ratings.add(averageRating);
                    ownersWithRatings.put(product.getOwner(), ratings);
                }
            }
        }

        for (Map.Entry<String, List<Double>> ownerRatings : ownersWithRatings.entrySet()) {
            String owner = ownerRatings.getKey();
            List<Double> ratings = ownerRatings.getValue();

            double sum = 0;

            for (Double rating : ratings) {
                sum += rating;
            }

            double totalAverageRating = sum / ratings.size();


            mailService.sendMail(SendMailDto.builder()
                    .subject("Products total rating for last 1 minute")
                    .text("You average rating is %s".formatted(totalAverageRating))
                    .recipient(owner)
                    .build());
        }
    }

    // fixedDelay = 1 hour
    // 1 job - 13:00 - 13:05
    // 2 job - 14:05 - 14:10
    // 3 job - 15:10 - 15:15

    // fixedRate = 1 hour
    // 1 job - 13:00 - 13:40
    // 2 job - 14:00 - 14:05
    // 3 job - 15:00 - 15:05
}
