package com.smartshop.controller;

import com.smartshop.service.ClientService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Access restricted only to admin.");
        }
        clientService.deleteClient(id);
    }
}
