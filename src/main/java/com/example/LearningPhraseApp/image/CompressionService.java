package com.example.LearningPhraseApp.image;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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
            int width = image.getWidth();
            int height = image.getHeight();
            double valueWidth = (double) width / maxWidth;
            double valueHeight = (double) height / maxHeight;
            if (valueWidth >= valueHeight) {
                width = maxWidth;
                height = (int) (height / valueWidth);
            } else {
                height = maxHeight;
                width = (int) (width / valueHeight);
            }

            Thumbnails.Builder<BufferedImage> thumbnailBuilder = Thumbnails.of(image)
                    .size(width, height);

            BufferedImage resizedImage = thumbnailBuilder.asBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, imageType, baos);

            byteObjects = baos.toByteArray();

            return byteObjects;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
