package com.smartshop.controller;

import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;
import com.smartshop.dto.response.ClientStatisticsDTO;
import com.smartshop.exception.UnauthorizedAccessException;
import com.smartshop.service.ClientService;
import com.smartshop.service.ClientStatisticsService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientStatisticsService clientStatisticsService;

    @GetMapping
    public Page<ClientResponseDTO> getAllClients(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        return clientService.getAllClients(page, size);
    }

    @GetMapping("/{id}")
    public ClientResponseDTO getClientById(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    public ClientResponseDTO updateClient(
            @PathVariable Long id,
            @RequestBody ClientUpdateDTO dto,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        return clientService.updateClient(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        clientService.deleteClient(id);
    }

    @GetMapping("/{id}/statistics")
    public ClientStatisticsDTO getClientStatistics(
            @PathVariable Long id,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        return clientStatisticsService.getClientStatistics(id);
    }

    @GetMapping("/personalinfo")
    public ClientResponseDTO getPersonalInfo(HttpServletRequest request) {
        if (!SessionUtil.isClient(request)) {
            throw new UnauthorizedAccessException("Client access required");
        }
        Long userId = SessionUtil.getUserId(request);
        return clientService.getClientByUserId(userId);
    }
}

