package com.example.LearningPhraseApp.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
class CompressionService implements ImageProcessingService {

    public byte[] adjustImage(MultipartFile file, int maxWidth, int maxHeight, String imageType) {
        try {
            byte[] byteObjects = file.getBytes();
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteObjects));
            BufferedImage resizedImage = scaleImage(image,maxWidth,maxHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, imageType, baos);

            byteObjects = baos.toByteArray();
            return byteObjects;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private BufferedImage scaleImage(BufferedImage image, int maxWidth, int maxHeight) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        double valueWidth = (double) width / maxWidth;
        double valueHeight = (double) height / maxHeight;
        int newWidth, newHeight;

        if (valueWidth >= valueHeight) {
            newWidth = maxWidth;
            newHeight = (int) (height / valueWidth);
        } else {
            newHeight = maxHeight;
            newWidth = (int) (width / valueHeight);
        }

        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        return bufferedImage;
    }

}
