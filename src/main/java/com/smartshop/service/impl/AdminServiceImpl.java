package com.smartshop.service.impl;

import com.smartshop.entity.User;
import com.smartshop.entity.enums.UserRole;
import com.smartshop.exception.CannotDeleteAdminException;
import com.smartshop.exception.ClientNotFoundException;
import com.smartshop.exception.UserDeletionException;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AdminService;
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
                .orElseThrow(() -> new ClientNotFoundException("Admin not found with id: " + id));

        if (user.getRole() != UserRole.ADMIN) {
            throw new UserDeletionException("Could not delete");
        }
        
        if ("admin0".equals(user.getUsername())) {
            throw new CannotDeleteAdminException("Cannot delete the primary admin");
        }
        
        userRepository.delete(user);
    }
}
