package com.smartshop.controller;

import com.smartshop.exception.UnauthorizedAccessException;
import com.smartshop.service.AdminService;
import com.smartshop.util.SessionUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        if (! SessionUtil.isAdmin(request)) {
            throw new UnauthorizedAccessException("Access restricted only to admin.");
        }
        adminService.deleteAdmin(id);
    }
}
