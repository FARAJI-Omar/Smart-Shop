package com.smartshop.repository;

import com.smartshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // all orders by client id
    @Query("SELECT o FROM Order o WHERE o.client.id = :clientId")
    List<Order> findClientsOrders(@Param("clientId") Long clientId);

    // count all orders by client id
    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId")
    Long countByClientId(@Param("clientId") Long clientId);

    // count confirmed orders by client id
    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    Long countConfirmedOrdersByClientId(@Param("clientId") Long clientId);

    // sum total amount of confirmed orders by client id
    @Query("SELECT COALESCE(SUM(o.total), 0.0) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    Double sumTotalByClientIdAndStatusConfirmed(@Param("clientId") Long clientId);

    // first order date (confirmed orders only)
    @Query("SELECT MIN(o.date) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    LocalDateTime findFirstOrderDateByClientId(@Param("clientId") Long clientId);

    // last order date (confirmed orders only)
    @Query("SELECT MAX(o.date) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    LocalDateTime findLastOrderDateByClientId(@Param("clientId") Long clientId);
}
