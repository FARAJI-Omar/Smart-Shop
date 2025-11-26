package com.smartshop.service;

import com.smartshop.dto.response.ClientResponseDTO;

public interface ClientService {
    void deleteClient(Long id);
    ClientResponseDTO getClientById(Long id);
}
