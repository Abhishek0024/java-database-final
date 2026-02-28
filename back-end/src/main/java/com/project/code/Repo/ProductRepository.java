package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 2. Retrieve all products
    // NOTE: findAll() already exists in JpaRepository
    List<Product> findAll();

    // Find products by category
    List<Product> findByCategory(String category);

    // Find products within price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find product by SKU
    Product findBySku(String sku);

    // Find product by name
    Product findByName(String name);

    // Custom Query:
    // Find products available in a specific store matching name pattern
    @Query("""
           SELECT p
           FROM Product p
           JOIN Inventory i ON i.product.id = p.id
           WHERE i.store.id = :storeId
           AND LOWER(p.name) LIKE LOWER(CONCAT('%', :pname, '%'))
           """)
    List<Product> findByNameLike(
            @Param("storeId") Long storeId,
            @Param("pname") String pname
    );
}