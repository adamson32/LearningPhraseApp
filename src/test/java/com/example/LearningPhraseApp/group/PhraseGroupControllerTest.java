package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.cloudinary.CloudinaryService;
import com.example.LearningPhraseApp.group.dto.PhraseGroupWriteDto;
import com.example.LearningPhraseApp.image.ImageProcessingService;
import com.example.LearningPhraseApp.image.ImageValidationService;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhraseGroupControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhraseGroupRepository phraseGroupRepository;

    @Mock
    private ImageValidationService imageValidationService;

    @Mock
    private ImageProcessingService imageProcessingService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private PhraseGroupController phraseGroupController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create() throws IOException {
        PhraseGroupWriteDto current = new PhraseGroupWriteDto();

        byte[] imageData = Files.readAllBytes(Paths.get("src/main/resources/testImage.jpg"));
        byte[] imageData2 = Files.readAllBytes(Paths.get("src/main/resources/test.txt"));
        FileInputStream inputFile = new FileInputStream( "src/main/resources/testImage.jpg");

        MockMultipartFile file = new MockMultipartFile("file.jpg", "filename.jpg", "image/jpeg", inputFile);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");
        assertEquals(imageValidationService.isImageFileTypeValid(file),false);
        assertEquals(imageValidationService.isValidImage(file),false);
        when(imageValidationService.isValidImage(file)).thenReturn(true);

        User user = new User();
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(user));

        byte[] adjustedImage = new byte[10];
        when(imageProcessingService.adjustImage(file, 700, 500, "png")).thenReturn(adjustedImage);

        String imageUrl = "http://example.com/image.png";
        when(cloudinaryService.uploadImage(adjustedImage)).thenReturn(imageUrl);

        String result = phraseGroupController.create(current, mock(BindingResult.class), file, authentication);

        assertEquals("redirect:/groupPhrases", result);
        verify(phraseGroupRepository, times(1)).save(any(PhraseGroup.class));
    }
    }


