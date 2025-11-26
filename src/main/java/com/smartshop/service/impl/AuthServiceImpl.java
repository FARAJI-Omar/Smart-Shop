package com.smartshop.service.impl;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.User;
import com.smartshop.entity.enums.UserRole;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AuthService;
import com.smartshop.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Override
    public UserResponseDTO createAdmin(UserCreateDTO userCreateDTO) {
        User user = userMapper.toEntity(userCreateDTO);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRole(UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}
