package com.smartshop.service.impl;

import com.smartshop.entity.User;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
        
        if ("admin0".equals(user.getUsername())) {
            throw new IllegalStateException("Cannot delete the primary admin");
        }
        
        userRepository.delete(user);
    }
}
