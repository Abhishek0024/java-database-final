package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    // 2. Autowired Dependencies
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    // 3. Add Product
    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateProduct(product)) {
            response.put("message", "Product already exists");
            return response;
        }

        try {
            productRepository.save(product);
            response.put("message", "Product added successfully");
        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Duplicate SKU or invalid data");
        }

        return response;
    }

    // 4. Get Product by ID
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(
            @PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();

        Product product = productRepository.findById(id)
                .orElse(null);

        response.put("products", product);
        return response;
    }

    // 5. Update Product
    @PutMapping
    public Map<String, String> updateProduct(
            @RequestBody Product product) {

        Map<String, String> response = new HashMap<>();

        productRepository.save(product);

        response.put("message",
                "Product updated successfully");

        return response;
    }

    // 6. Filter by Name & Category
    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(
            @PathVariable String name,
            @PathVariable String category) {

        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equalsIgnoreCase(name)
                && !"null".equalsIgnoreCase(category)) {

            products =
                    productRepository.findByCategory(category);

        } else if (!"null".equalsIgnoreCase(name)
                && "null".equalsIgnoreCase(category)) {

            products =
                    List.of(productRepository.findByName(name));

        } else {
            products = productRepository.findAll();
        }

        response.put("products", products);
        return response;
    }

    // 7. List All Products
    @GetMapping
    public Map<String, Object> listProduct() {

        Map<String, Object> response = new HashMap<>();

        response.put("products",
                productRepository.findAll());

        return response;
    }

    // 8. Filter by Category and Store
    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(
            @PathVariable String category,
            @PathVariable Long storeid) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                productRepository.findByNameLike(
                        storeid,
                        category);

        response.put("product", products);
        return response;
    }

    // 9. Delete Product
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(
            @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            response.put("message", "Product not found");
            return response;
        }

        inventoryRepository.deleteByProduct_Id(id);
        productRepository.deleteById(id);

        response.put("message",
                "Product deleted successfully");

        return response;
    }

    // 10. Search Product by Name
    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(
            @PathVariable String name) {

        Map<String, Object> response = new HashMap<>();

        Product product =
                productRepository.findByName(name);

        response.put("products", product);
        return response;
    }
}