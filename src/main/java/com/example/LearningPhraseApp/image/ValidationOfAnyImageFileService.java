package com.example.LearningPhraseApp.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
class ValidationOfAnyImageFileService implements ImageValidationService {

    public static final int maxImageFileSize = 10 * 1024 * 1024;

    @Override
    public boolean isImageFileSizeValid(MultipartFile file) {
        return file.getSize() < maxImageFileSize;
    }

    @Override
    public boolean isImageFileTypeValid(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).startsWith("image/");
    }


}
