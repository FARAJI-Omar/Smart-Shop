package com.smartshop.util;

import com.smartshop.entity.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        UserRole role = (UserRole) session.getAttribute("userRole");
        return role == UserRole.ADMIN;
    }

    public static boolean isClient(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        UserRole role = (UserRole) session.getAttribute("userRole");
        return role == UserRole.CLIENT;
    }
}
