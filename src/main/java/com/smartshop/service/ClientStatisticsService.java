package com.smartshop.service;

import com.smartshop.dto.response.ClientStatisticsDTO;

public interface ClientStatisticsService {
    ClientStatisticsDTO getClientStatistics(Long clientId);
}

