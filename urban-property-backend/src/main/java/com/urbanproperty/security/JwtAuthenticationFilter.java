package com.urbanproperty.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Extract the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. Check if the header is valid
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                // 3. Extract the token
                String jwt = authHeader.substring(7);

                // 4. Delegate to JwtUtils to validate and create the Authentication object
                Authentication authentication = jwtUtils.populateAuthenticationTokenFromJWT(jwt);
                
                // 5. Set the Authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Successfully authenticated user '{}' and set security context", authentication.getName());

            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
            }
        }
        
        // 6. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}