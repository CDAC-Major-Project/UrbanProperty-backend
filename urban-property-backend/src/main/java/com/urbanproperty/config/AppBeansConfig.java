package com.urbanproperty.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cloudinary.Cloudinary;
import com.urbanproperty.dto.PropertyResponseDto;
import com.urbanproperty.entities.Property;
import com.urbanproperty.entities.PropertyImage;
@Configuration
public class AppBeansConfig {
	@Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

	@Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");
        return new Cloudinary(config);
    }
    @Bean
    public ModelMapper modelMapper() {
        // 1. Create the ModelMapper instance and apply your general settings first.
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // Your excellent strict matching
                .setPropertyCondition(Conditions.isNotNull());    // Your excellent setting to skip nulls

        // 2. Now, add the specific mapping logic for Property -> PropertyResponseDto.
        TypeMap<Property, PropertyResponseDto> typeMap = modelMapper.createTypeMap(Property.class, PropertyResponseDto.class);
        
        // This converter handles the Set<PropertyImage> -> Set<String> conversion
        Converter<Set<PropertyImage>, Set<String>> imagesConverter = ctx ->
            ctx.getSource() == null ? null : ctx.getSource().stream()
                                                .map(PropertyImage::getImageUrl)
                                                .collect(Collectors.toSet());

        // Apply the custom converter to the 'images' field mapping
        typeMap.addMappings(mapper -> mapper.using(imagesConverter).map(Property::getImages, PropertyResponseDto::setImages));

        // 3. Return the fully configured bean.
        return modelMapper;
    }
  //configure PasswordEncoder as spring bean
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}