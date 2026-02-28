package com.project.code.Repo;

import com.project.code.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 2. Find customer by email
    Customer findByEmail(String email);

    // 2. Find customer by ID
    // NOTE: JpaRepository already provides findById(Long id) 
    // but it returns Optional<Customer>
    Optional<Customer> findById(Long id);

    // 3. Additional Custom Query Methods

    List<Customer> findByName(String name);

    List<Customer> findByPhone(String phone);
}