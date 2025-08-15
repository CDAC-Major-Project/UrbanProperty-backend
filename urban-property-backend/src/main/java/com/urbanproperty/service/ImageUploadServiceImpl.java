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
    @Override
    public void deleteImage(String publicId) throws IOException {
        // Used the 'destroy' method from the uploader to delete by public_id
        cloudinary.uploader().destroy(publicId, new HashMap<>());
    }
}