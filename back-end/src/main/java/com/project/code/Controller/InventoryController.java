package com.project.code.Controller;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import com.project.code.DTO.CombinedRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    // 2. Autowired Dependencies
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    // 3. Update Inventory
    @PutMapping("/update")
    public Map<String, String> updateInventory(
            @RequestBody CombinedRequest request) {

        Map<String, String> response = new HashMap<>();

        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        if (!serviceClass.validateProductId(product.getId())) {
            response.put("message", "Invalid Product ID");
            return response;
        }

        Inventory existingInventory =
                serviceClass.getInventoryId(inventory);

        if (existingInventory != null) {
            existingInventory.setStockLevel(
                    inventory.getStockLevel());

            inventoryRepository.save(existingInventory);

            response.put("message",
                    "Inventory updated successfully");
        } else {
            response.put("message",
                    "No inventory data available");
        }

        return response;
    }

    // 4. Save Inventory
    @PostMapping("/save")
    public Map<String, String> saveInventory(
            @RequestBody Inventory inventory) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateInventory(inventory)) {
            response.put("message",
                    "Inventory already exists");
            return response;
        }

        inventoryRepository.save(inventory);
        response.put("message",
                "Inventory saved successfully");

        return response;
    }

    // 5. Get Products for Store
    @GetMapping("/products/{storeId}")
    public Map<String, List<Inventory>> getAllProducts(
            @PathVariable Long storeId) {

        Map<String, List<Inventory>> response = new HashMap<>();

        List<Inventory> inventories =
                inventoryRepository.findByStore_Id(storeId);

        response.put("products", inventories);
        return response;
    }

    // 6. Filter Products by Category & Name
    @GetMapping("/filter/{category}/{name}")
    public Map<String, Object> getProductName(
            @PathVariable String category,
            @PathVariable String name) {

        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equalsIgnoreCase(category)
                && !"null".equalsIgnoreCase(name)) {

            products = productRepository.findByName(name);

        } else if (!"null".equalsIgnoreCase(category)
                && "null".equalsIgnoreCase(name)) {

            products =
                    productRepository.findByCategory(category);

        } else {
            products = productRepository.findAll();
        }

        response.put("product", products);
        return response;
    }

    // 7. Search Product by Name in Store
    @GetMapping("/search")
    public Map<String, Object> searchProduct(
            @RequestParam String name,
            @RequestParam Long storeId) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                productRepository.findByNameLike(
                        storeId, name);

        response.put("product", products);
        return response;
    }

    // 8. Remove Product
    @DeleteMapping("/remove/{productId}")
    public Map<String, String> removeProduct(
            @PathVariable Long productId) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateProductId(productId)) {
            response.put("message", "Product not found");
            return response;
        }

        inventoryRepository.deleteByProduct_Id(productId);
        productRepository.deleteById(productId);

        response.put("message",
                "Product deleted successfully");

        return response;
    }

    // 9. Validate Quantity
    @GetMapping("/validateQuantity")
    public boolean validateQuantity(
            @RequestParam Long productId,
            @RequestParam Long storeId,
            @RequestParam Integer quantity) {

        Inventory inventory =
                inventoryRepository
                        .findByProduct_IdAndStore_Id(
                                productId,
                                storeId);

        return inventory != null &&
                inventory.getStockLevel() >= quantity;
    }
}