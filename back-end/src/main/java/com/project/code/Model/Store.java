package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(name = "store")
public class Store {

    // 1. Primary Key - Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Name field
    @NotNull(message = "Store name cannot be null")
    @Column(nullable = false)
    private String name;

    // 3. Address field
    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    @Column(nullable = false)
    private String address;

    // 4. One-to-Many Relationship with Inventory
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventoryList;

    // 5. Default Constructor (Required by JPA)
    public Store() {
    }

    // 5. Custom Constructor
    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // 7. Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}