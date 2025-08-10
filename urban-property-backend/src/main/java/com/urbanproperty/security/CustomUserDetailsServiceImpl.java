package com.urbanproperty.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.dao.UserDao;
import com.urbanproperty.entities.UserEntity;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	//depcy
	private UserDao userDao;

	//for spring security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Log that the method is being called
        System.out.println("in load by user nm " + email);
        
        // Fetch the user from the database by their email (which we use as the username)
        UserEntity user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Since UserEntity implements UserDetails, we can return it directly.
        return user;
    }

}
