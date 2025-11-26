package com.smartshop.service;

import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;

public interface ClientService {
    void deleteClient(Long id);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientUpdateDTO dto);
}
