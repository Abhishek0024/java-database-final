package com.project.code.Repo;

import com.project.code.Model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    // 2. Find reviews by Store ID and Product ID
    List<Review> findByStoreIdAndProductId(Long storeId, Long productId);

}