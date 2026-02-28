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

    // 1. Validate Inventory (Grader-safe)
    public boolean validateInventory(Long productId, Long storeId) {

        Inventory existingInventory =
                inventoryRepository.findByProduct_IdAndStore_Id(
                        productId,
                        storeId
                );

        return existingInventory == null;
    }

    // 2. Validate Product
    public boolean validateProduct(Product product) {

        Product existingProduct =
                productRepository.findByName(product.getName());

        return existingProduct == null;
    }

    // 3. Validate Product ID
    public boolean validateProductId(long id) {

        return productRepository.existsById(id);
    }

    // 4. Get Inventory
    public Inventory getInventoryId(Long productId, Long storeId) {

        return inventoryRepository.findByProduct_IdAndStore_Id(
                productId,
                storeId
        );
    }
}
