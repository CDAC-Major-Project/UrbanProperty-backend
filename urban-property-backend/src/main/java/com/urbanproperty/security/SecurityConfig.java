package com.urbanproperty.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	//for same-wifi
        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 1. Disable CSRF, as it's not needed for stateless REST APIs
            .csrf(csrf -> csrf.disable())
            // 2. Define authorization rules for all HTTP requests
            .authorizeHttpRequests(auth -> auth
                // -- Whitelist Public Endpoints --
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, 
                    "/api/v1/properties", 
                    "/api/v1/properties/{id}",
                    "/api/v1/amenities", 
                    "/api/v1/amenities/{id}",
                    "/api/v1/property-types", 
                    "/api/v1/property-types/{id}"
                ).permitAll()
                .requestMatchers(HttpMethod.OPTIONS).permitAll() // For CORS pre-flight requests from frontend
                
                // -- Secure All Other Endpoints --
                .anyRequest().authenticated()
            )
            // 3. Set session management to STATELESS since we are using JWTs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 4. Add your custom JWT filter to the security chain
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // 5. Expose the AuthenticationManager as a Spring Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // This bean defines the CORS configuration for the application.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // IMPORTANT: Define the origins of your frontend application
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",      // Vite default
                "http://10.160.36.103:3000",  // 
                "http://192.168.2.123:5173"  // ✅ ADD THIS LINE - Your Teammate's Frontend
        ));
     // Define which HTTP methods are allowed
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Define which headers are allowed
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));

        // Allow credentials (e.g., cookies, authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths in your application
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}