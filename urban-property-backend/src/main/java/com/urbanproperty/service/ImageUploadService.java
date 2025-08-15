package com.urbanproperty.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    Map uploadImage(MultipartFile file, String publicId) throws IOException;
    void deleteImage(String publicId) throws IOException;
}