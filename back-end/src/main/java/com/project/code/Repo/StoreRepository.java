package com.project.code.Repo;

import com.project.code.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // 2. Find store by ID
    // NOTE: JpaRepository already provides findById(Long id)
    Optional<Store> findById(Long id);

    // 2. Find stores by substring in name (case-insensitive)
    @Query("""
           SELECT s
           FROM Store s
           WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :pname, '%'))
           """)
    List<Store> findBySubName(@Param("pname") String pname);

}