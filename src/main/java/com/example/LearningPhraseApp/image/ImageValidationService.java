package com.example.LearningPhraseApp.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public interface ImageValidationService {

    boolean isImageFileSizeValid(MultipartFile file);

    boolean isImageFileTypeValid(MultipartFile file);

    default boolean isValidImage(MultipartFile file) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if (!isImageFileTypeValid(file)) {
            logger.error("File type is not valid");
            return false;
        }
        if (!isImageFileSizeValid(file)) {
            logger.error("Image size is too large");
            return false;
        }
        return true;
    }
}
