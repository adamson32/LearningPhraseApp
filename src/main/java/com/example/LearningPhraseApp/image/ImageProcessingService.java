package com.example.LearningPhraseApp.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageProcessingService {
    byte[] adjustImage(MultipartFile file, int maxWidth, int maxHeight, String imageType);
}
