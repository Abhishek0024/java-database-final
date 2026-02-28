package com.project.code.Controller;

import com.project.code.Model.Review;
import com.project.code.Model.Customer;
import com.project.code.Repo.ReviewRepository;
import com.project.code.Repo.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    // 2. Autowired Dependencies
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // 3. Get Reviews by Store and Product
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> filteredReviews = new ArrayList<>();

        List<Review> reviews =
                reviewRepository.findByStoreIdAndProductId(
                        storeId,
                        productId
                );

        for (Review review : reviews) {

            Map<String, Object> reviewData = new HashMap<>();

            // Fetch customer name
            Customer customer =
                    customerRepository
                            .findById(review.getCustomerId())
                            .orElse(null);

            reviewData.put("comment", review.getComment());
            reviewData.put("rating", review.getRating());
            reviewData.put(
                    "customerName",
                    customer != null
                            ? customer.getName()
                            : "Unknown Customer"
            );

            filteredReviews.add(reviewData);
        }

        response.put("reviews", filteredReviews);
        return response;
    }
}