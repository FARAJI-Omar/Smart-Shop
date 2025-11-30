package com.smartshop.service;

import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;
import org.springframework.data.domain.Page;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface ClientService {
    void deleteClient(Long id);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientUpdateDTO dto);
    Page<ClientResponseDTO> getAllClients(int page, int size);
}
