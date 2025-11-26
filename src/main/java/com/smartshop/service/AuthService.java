package com.smartshop.service;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UserResponseDTO createAdmin(UserCreateDTO userCreateDTO);
}
