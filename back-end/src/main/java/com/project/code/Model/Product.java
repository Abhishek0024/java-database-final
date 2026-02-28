package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(
    name = "product",
    uniqueConstraints = @UniqueConstraint(columnNames = "sku")
)
public class Product {

    // 1. Primary Key - Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Name field
    @NotNull(message = "Product name cannot be null")
    @Column(nullable = false)
    private String name;

    // 3. Category field
    @NotNull(message = "Category cannot be null")
    @Column(nullable = false)
    private String category;

    // 4. Price field
    @NotNull(message = "Price cannot be null")
    @Column(nullable = false)
    private Double price;

    // 5. SKU field (Unique)
    @NotNull(message = "SKU cannot be null")
    @Column(nullable = false, unique = true)
    private String sku;

    // 6. One-to-Many Relationship with Inventory
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventoryList;

    // Default Constructor (Required by JPA)
    public Product() {
    }

    // 8. Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}