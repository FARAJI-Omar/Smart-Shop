package com.smartshop.service.impl;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.request.UserCreateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.User;
import com.smartshop.entity.enums.CustomerTier;
import com.smartshop.entity.enums.UserRole;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AuthService;
import com.smartshop.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;
    
    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        if (dto.getRole() == UserRole.CLIENT) {
            Client client = new Client();
            client.setUser(savedUser);
            client.setName(savedUser.getUsername());
            client.setEmail(dto.getEmail());
            client.setLoyaltyLevel(CustomerTier.BASIC);
            clientRepository.save(client);
        }
        
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserResponseDTO login(LoginDTO loginDTO, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!PasswordUtil.verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("userRole", user.getRole());

        return userMapper.toDTO(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
