package com.urbanproperty.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.urbanproperty.dao.UserDao;

@Service("customSecurity")
public class CustomSecurityExpression {

    @Autowired
    private UserDao userDao;

    /**
     * Checks if the authenticated user is the owner of the account being accessed
     * OR if the authenticated user is an ADMIN.
     */
    public boolean isOwnerOrAdmin(Authentication authentication, Long userId) {
        // Grant access if the user is an ADMIN
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        // Otherwise, check if they are the owner by comparing emails
        String loggedInUserEmail = authentication.getName();
        return userDao.findById(userId)
                .map(user -> user.getEmail().equals(loggedInUserEmail))
                .orElse(false);
    }
}