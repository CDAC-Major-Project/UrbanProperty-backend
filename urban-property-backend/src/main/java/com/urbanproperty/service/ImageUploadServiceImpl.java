package com.urbanproperty.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public Map uploadImage(MultipartFile file, String publicId) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("overwrite", false); // Prevent overwriting existing images

        return this.cloudinary.uploader().upload(file.getBytes(), options);
    }
}