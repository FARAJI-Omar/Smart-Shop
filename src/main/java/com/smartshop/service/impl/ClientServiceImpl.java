package com.smartshop.service.impl;

import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.exception.ClientNotFoundException;
import com.smartshop.mapper.ClientMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        
        Long userId = client.getUser().getId();
        clientRepository.deleteById(id);
        userRepository.deleteById(client.getUser().getId());
    }

    @Override
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        return clientMapper.toDTO(client);
    }

    @Override
    public ClientResponseDTO getClientByUserId(Long userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        return clientMapper.toDTO(client);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        
        if (dto.getName() != null) {
            client.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            client.setEmail(dto.getEmail());
        }
        
        Client updated = clientRepository.save(client);
        return clientMapper.toDTO(updated);
    }

    @Override
    public Page<ClientResponseDTO> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(clientMapper::toDTO);
    }
}
