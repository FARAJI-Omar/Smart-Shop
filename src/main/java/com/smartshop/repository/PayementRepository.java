package com.smartshop.repository;

import com.smartshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayementRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT COALESCE(MAX(p.paymentNumber), 0) + 1 FROM Payment p WHERE p.order.id = :orderId")
    Integer getNextPaymentNumber(@Param("orderId") Long orderId);

    // Find payments by order ID ordered by payment number ascending
    List<Payment> findByOrderIdOrderByPaymentNumberAsc(Long orderId);
}
