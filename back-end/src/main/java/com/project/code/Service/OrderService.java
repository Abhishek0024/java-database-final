package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import com.project.code.DTO.PlaceOrderRequestDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            CustomerRepository customerRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            OrderDetailsRepository orderDetailsRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // 1. Save Order
    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        // 2. Retrieve or Create Customer
        Customer customer =
                customerRepository.findByEmail(placeOrderRequest.getEmail());

        if (customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequest.getCustomerName());
            customer.setEmail(placeOrderRequest.getEmail());
            customer.setPhone(placeOrderRequest.getPhone());
            customer = customerRepository.save(customer);
        }

        // 3. Retrieve Store
        Store store = storeRepository.findById(placeOrderRequest.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // 4. Create OrderDetails
        OrderDetails order = new OrderDetails();
        order.setCustomer(customer);
        order.setStore(store);
        order.setDate(LocalDateTime.now());
        order.setTotalPrice(0.0);

        order = orderDetailsRepository.save(order);

        double totalPrice = 0.0;

        // 5. Process Order Items
        for (PlaceOrderRequestDTO.OrderItemDTO itemDTO :
                placeOrderRequest.getItems()) {

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found"));

            Inventory inventory =
                    inventoryRepository.findByProduct_IdAndStore_Id(
                            product.getId(),
                            store.getId()
                    );

            if (inventory == null ||
                    inventory.getStockLevel() < itemDTO.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            // Update stock
            inventory.setStockLevel(
                    inventory.getStockLevel() - itemDTO.getQuantity());
            inventoryRepository.save(inventory);

            // Create OrderItem
            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    itemDTO.getQuantity(),
                    product.getPrice()
            );

            orderItemRepository.save(orderItem);

            totalPrice += product.getPrice()
                    * itemDTO.getQuantity();
        }

        // Update total price
        order.setTotalPrice(totalPrice);
        orderDetailsRepository.save(order);
    }
}