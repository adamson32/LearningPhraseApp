package com.example.LearningPhraseApp.image;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CompressionServiceTest {
    private CompressionService compressionService;

    @BeforeEach
    public void setUp() {
        compressionService = new CompressionService();
    }
    @ParameterizedTest
    @MethodSource("provideDimensions")
    void should_adjust_image_to_given_dimensions(int x, int y) {
        try{
            byte[] imageData = Files.readAllBytes(Paths.get("src/main/resources/testImage.jpg"));
            MockMultipartFile mockFile = new MockMultipartFile("image.jpg",imageData);
            byte[] result = compressionService.adjustImage(mockFile,x,y,"png");
            BufferedImage resizedImage = ImageIO.read(new ByteArrayInputStream(result));
            int resizedImageWidth = resizedImage.getWidth();
            int resizedImageHeight = resizedImage.getHeight();
            double aspectRatio = (double) resizedImageWidth/resizedImageHeight;
            assertTrue(resizedImageWidth == x || resizedImageHeight == y);
            assertEquals(1794.0/1090.0,aspectRatio,0.005);
        }catch (IOException e){
            fail("Exception occurred" + e.getMessage());
        }
    }
    private static Stream<Arguments> provideDimensions() {
        return Stream.of(
                Arguments.of(700,500),
                Arguments.of(800,300),
                Arguments.of(300,1400),
                Arguments.of(2000,2000)
        );
    }

}