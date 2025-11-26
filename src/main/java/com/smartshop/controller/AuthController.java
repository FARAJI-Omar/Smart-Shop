package com.smartshop.controller;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/createadmin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createAdmin(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return authService.createAdmin(userCreateDTO);
    }
    
    @PostMapping("/login")
    public UserResponseDTO login(@Valid @RequestBody LoginDTO loginDTO,
                                 HttpServletRequest request) {
        return authService.login(loginDTO, request);
    }
}
