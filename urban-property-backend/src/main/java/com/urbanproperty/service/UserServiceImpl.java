package com.urbanproperty.service;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanproperty.custom_exceptions.ApiException;
import com.urbanproperty.custom_exceptions.ResourceNotFoundException;
import com.urbanproperty.dao.PropertyDao;
import com.urbanproperty.dao.UserDao;
import com.urbanproperty.dto.AllUsersResponse;
import com.urbanproperty.dto.LoginRequestDto;
import com.urbanproperty.dto.LoginResponseDto;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.dto.UserRegistrationRequest;
import com.urbanproperty.dto.UserResponse;
import com.urbanproperty.dto.admin.AdminDashboardStatsDTO;
import com.urbanproperty.dto.admin.MonthlyUserStatsDTO;
import com.urbanproperty.dto.admin.UserMonthlyStats;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.Role;
import com.urbanproperty.entities.UserEntity;
import com.urbanproperty.security.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PropertyDao propertyDao;
    private final ModelMapper mapper;
    // Inject the PasswordEncoder bean
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // Added
    private final JwtUtils jwtUtils; // Added
    

    @Override
    public UserResponse registerUser(UserRegistrationRequest dto) {
    	// Block ADMIN registration via the public API
        if (dto.getRole() == Role.ADMIN) {
            throw new ApiException("Admin registration is not allowed.");
        }
        
        if (userDao.existsByEmail(dto.getEmail()))
            throw new ApiException("Dup Email detected - User exists already!!!!");

        // Check if a user with the same phone number already exists
        if (userDao.existsByPhoneNumber(dto.getPhoneNumber())) {
            // UPDATED: More generic and secure error message
            throw new ApiException("Phone number is already registered.");
        }
        
        UserEntity entity = mapper.map(dto, UserEntity.class);
        String password = entity.getPassword();
        entity.setPassword(passwordEncoder.encode(password));
        UserEntity savedEntity = userDao.save(entity);
        return mapper.map(savedEntity, UserResponse.class);
    }
//
    @Override
    public LoginResponseDto loginUser(LoginRequestDto request) {
    	// 1. Authenticate using Spring Security's AuthenticationManager
        // This will use your loadUserByUsername and PasswordEncoder automatically
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. If authentication is successful, generate the JWT token
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        // 3. Get the user details from the authentication principal
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserResponse userDetails = mapper.map(user, UserResponse.class);
        
        return new LoginResponseDto("Login successful!", userDetails);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapper.map(user, UserResponse.class);
    }
//
    @Override
    public AllUsersResponse getAllUsers() {
        List<UserEntity> allUsers = userDao.findAll();

        // Use Java Streams to group users by their role into a Map
        Map<Role, List<UserResponse>> usersByRole = allUsers.stream()
                .filter(user -> user.getRole() == Role.BUYER || user.getRole() == Role.SELLER) // Exclude ADMINs
                .collect(Collectors.groupingBy(
                		UserEntity::getRole,
                        Collectors.mapping(user -> mapper.map(user, UserResponse.class), Collectors.toList())
                ));

        // Create the response object
        AllUsersResponse response = new AllUsersResponse();
        response.setBuyer(usersByRole.getOrDefault(Role.BUYER, Collections.emptyList()));
        response.setSeller(usersByRole.getOrDefault(Role.SELLER, Collections.emptyList()));

        return response;
    }
    
    //for for adding and removing property as favourite
    @Override
    public void addFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        
        user.addFavorite(property);
        // No need to call save, @Transactional will handle it.
    }

    @Override
    public void removeFavoriteProperty(Long userId, Long propertyId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        user.removeFavorite(property);
    }
    @Override
    public List<PropertyResponseDto> getFavoriteProperties(Long userId) {
        UserEntity user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return user.getFavoriteProperties().stream()
                // Assuming you have a helper method or can create one to map Property to PropertyResponseDto
                .map(property -> mapper.map(property, PropertyResponseDto.class))
                .collect(Collectors.toList());
    }
    
    /**
     * This is the main public method. It now clearly shows the three high-level steps:
     * 1. Fetch total users.
     */
    public AdminDashboardStatsDTO getDashboardStatistics() {
        long totalUsers = userDao.count();
        
        List<MonthlyUserStatsDTO> monthlyDataList = fetchAndProcessMonthlyStats();

        return new AdminDashboardStatsDTO(totalUsers, monthlyDataList);
    }

    //2. Fetch and process monthly stats.
    private List<MonthlyUserStatsDTO> fetchAndProcessMonthlyStats() {
        int currentYear = Year.now().getValue();
        List<UserMonthlyStats> rawStats = userDao.findMonthlyUserRegistrationStatsForYear(currentYear);

        // Using a TreeMap for sorted.
        Map<Integer, MonthlyUserStatsDTO> monthlyDataMap = new TreeMap<>();

        // The loop iterates through the raw data from the database.
        for (UserMonthlyStats stat : rawStats) {
            // For each statistic, get or create the DTO for that month.
            MonthlyUserStatsDTO monthDto = monthlyDataMap.computeIfAbsent(
                stat.getMonth(), 
                this::createMonthlyDtoForNumber // Use a method reference for even better readability
            );

            // Populate the correct count based on the user's role.
            if (stat.getRole() == Role.SELLER) {
                monthDto.setSellerCount((int) stat.getCount());
            } else if (stat.getRole() == Role.BUYER) {
                monthDto.setBuyerCount((int) stat.getCount());
            }
        }

        // Convert the map's values into the final list and return it.
        return new ArrayList<>(monthlyDataMap.values());
    }

    // 3.Assemble and return the final DTO.
    private MonthlyUserStatsDTO createMonthlyDtoForNumber(int monthNumber) {
        String monthName = Month.of(monthNumber)
                                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                .toUpperCase();
        return new MonthlyUserStatsDTO(monthName, 0, 0);
    }
}
