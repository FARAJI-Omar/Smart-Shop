package com.smartshop.repository;

import com.smartshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    // find non-deleted products
    List<Product> findByDeletedFalse();
}
