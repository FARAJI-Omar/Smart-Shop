package com.smartshop.service;

import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;
import org.springframework.data.domain.Page;

public interface ClientService {
    void deleteClient(Long id);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO getClientByUserId(Long userId);
    ClientResponseDTO updateClient(Long id, ClientUpdateDTO dto);
    Page<ClientResponseDTO> getAllClients(int page, int size);
}
