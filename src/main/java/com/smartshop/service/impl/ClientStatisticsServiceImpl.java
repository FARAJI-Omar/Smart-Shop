package com.smartshop.service.impl;

import com.smartshop.dto.response.ClientStatisticsDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.enums.CustomerTier;
import com.smartshop.exception.ClientNotFoundException;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.OrderRepository;
import com.smartshop.service.ClientStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClientStatisticsServiceImpl implements ClientStatisticsService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    @Override
    public ClientStatisticsDTO getClientStatistics(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        int totalOrders = orderRepository.countByClientId(clientId);

        int totalConfirmedOrders = orderRepository.countConfirmedOrdersByClientId(clientId);

        // Sum amount of confirmed order totals
        Double totalSpent = orderRepository.sumTotalByClientIdAndStatusConfirmed(clientId);
        totalSpent = totalSpent != null ? Math.round(totalSpent * 100.0) / 100.0 : 0.0;

        LocalDateTime firstOrderDate = orderRepository.findFirstOrderDateByClientId(clientId);

        LocalDateTime lastOrderDate = orderRepository.findLastOrderDateByClientId(clientId);

        // Calculate loyalty level based on confirmed orders and total spent
        CustomerTier loyaltyLevel = calculateLoyaltyLevel(totalConfirmedOrders, totalSpent);

        return ClientStatisticsDTO.builder()
                .clientId(client.getId())
                .loyaltyLevel(loyaltyLevel)
                .totalOrders(totalOrders)
                .totalConfirmedOrders(totalConfirmedOrders)
                .totalOrders(totalOrders)
                .totalConfirmedOrders(totalConfirmedOrders)
                .totalSpent(totalSpent)
                .firstOrderDate(firstOrderDate)
                .lastOrderDate(lastOrderDate)
                .build();
    }

    // calculate loyalty level based on confirmed orders and total spent
    private CustomerTier calculateLoyaltyLevel(int totalConfirmedOrders, double totalSpent) {
        if (totalConfirmedOrders >= 20 || totalSpent >= 15000.0) {
            return CustomerTier.PLATINUM;
        }
        if (totalConfirmedOrders >= 10 || totalSpent >= 5000.0) {
            return CustomerTier.GOLD;
        }
        if (totalConfirmedOrders >= 3 || totalSpent >= 1000.0) {
            return CustomerTier.SILVER;
        }
        return CustomerTier.BASIC;
    }
}

