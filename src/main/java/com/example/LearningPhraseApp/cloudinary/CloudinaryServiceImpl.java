package com.example.LearningPhraseApp.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private static final String folderName = "learningPhrases";
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String publicId = folderName + "/" + extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static String extractPublicId(String imageUrl) {
        int startIndex = imageUrl.lastIndexOf('/') + 1;
        int endIndex = imageUrl.lastIndexOf('.');
        return imageUrl.substring(startIndex, endIndex);
    }

    @Override
    public String uploadImage(byte[] imageBytes) {
        try {
            Map params = ObjectUtils.asMap("folder", folderName);
            Map uploadResult = cloudinary.uploader().upload(imageBytes, params);
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
