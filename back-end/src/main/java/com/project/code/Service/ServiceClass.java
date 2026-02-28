package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ServiceClass(InventoryRepository inventoryRepository,
                        ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    // 1. Validate Inventory
    // Returns false if inventory already exists
    public boolean validateInventory(Inventory inventory) {

        Inventory existingInventory =
                inventoryRepository.findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );

        return existingInventory == null;
    }

    // 2. Validate Product by Name
    // Returns false if product already exists
    public boolean validateProduct(Product product) {

        Product existingProduct =
                productRepository.findByName(product.getName());

        return existingProduct == null;
    }

    // 3. Validate Product ID
    // Returns false if product does NOT exist
    public boolean validateProductId(long id) {

        return productRepository.existsById(id);
    }

    // 4. Get Inventory by Product + Store
    public Inventory getInventoryId(Inventory inventory) {

        return inventoryRepository.findByProduct_IdAndStore_Id(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}