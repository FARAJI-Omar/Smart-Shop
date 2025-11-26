package com.smartshop.controller;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.service.AuthService;
import com.smartshop.util.SessionUtil;
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
    
    @PostMapping("/createuser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Access restricted only to admin.");
        }
        return authService.createUser(userCreateDTO);
    }
    
    @PostMapping("/login")
    public UserResponseDTO login(@Valid @RequestBody LoginDTO loginDTO,
                                 HttpServletRequest request) {
        return authService.login(loginDTO, request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }
}
