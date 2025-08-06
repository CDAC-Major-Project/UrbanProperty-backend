package com.urbanproperty.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.UserDao;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;
import com.urbanproperty.entities.UserEntity;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper mapper;

    @Override
    public UserResponse registerUser(UserRegistrationRequest dto) {
        if (userDao.existsByEmail(dto.getEmail()))
            throw new ApiException("Dup Email detected - User exists already!!!!");

        UserEntity entity = mapper.map(dto, UserEntity.class);
        UserEntity savedEntity = userDao.save(entity);
        return mapper.map(savedEntity, UserResponse.class);
    }
//
    @Override
    public UserResponse getUserById(Long userId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapper.map(user, UserResponse.class);
    }
//
    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> users = userDao.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }
}
