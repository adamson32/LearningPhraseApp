package com.example.LearningPhraseApp.cloudinary;

public interface CloudinaryService {

    void deleteImage(String imageUrl);
    String uploadImage(byte[] imageBytes);
}
