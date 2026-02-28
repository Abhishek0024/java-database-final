package com.project.code.Controller;

import com.project.code.Model.Store;
import com.project.code.DTO.PlaceOrderRequestDTO;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    // 2. Autowired Dependencies
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    // 3. Add Store
    @PostMapping
    public Map<String, String> addStore(
            @RequestBody Store store) {

        Map<String, String> response = new HashMap<>();

        storeRepository.save(store);

        response.put("message",
                "Store created successfully");

        return response;
    }

    // 4. Validate Store
    @GetMapping("/validate/{storeId}")
    public boolean validateStore(
            @PathVariable Long storeId) {

        return storeRepository.existsById(storeId);
    }

    // 5. Place Order
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(
            @RequestBody PlaceOrderRequestDTO request) {

        Map<String, String> response = new HashMap<>();

        try {
            orderService.saveOrder(request);
            response.put("message",
                    "Order placed successfully");
        } catch (Exception ex) {
            response.put("Error",
                    "Order processing failed: " + ex.getMessage());
        }

        return response;
    }
}