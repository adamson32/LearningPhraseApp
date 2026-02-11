package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.cloudinary.CloudinaryService;
import com.example.LearningPhraseApp.group.dto.PhraseGroupNameDto;
import com.example.LearningPhraseApp.group.dto.PhraseGroupWriteDto;
import com.example.LearningPhraseApp.image.ImageProcessingService;
import com.example.LearningPhraseApp.image.ImageValidationService;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.LearningPhraseApp.group.dto.PhraseGroupDtoMapper.mapPhraseGroupToPhraseGroupNameDto;
import static com.example.LearningPhraseApp.group.dto.PhraseGroupDtoMapper.mapPhraseGroupsToPhraseGroupWriteDto;

@Controller
@RequestMapping("/phraseGroup")
public class PhraseGroupController {

    private final PhraseGroupRepository phraseGroupRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageProcessingService imageProcessingService;
    private final ImageValidationService imageValidationService;
    Logger logger = LoggerFactory.getLogger(PhraseGroupController.class);


    public PhraseGroupController(PhraseGroupRepository phraseGroupRepository,
                                 UserRepository userRepository, CloudinaryService cloudinaryService,
                                 ImageProcessingService imageProcessingService,
                                 ImageValidationService imageValidationService) {
        this.phraseGroupRepository = phraseGroupRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageProcessingService = imageProcessingService;
        this.imageValidationService = imageValidationService;
    }

    @GetMapping()
    String showTemplate(Model model) {
        model.addAttribute("phraseGroup", new PhraseGroupWriteDto());
        return "phraseGroup";
    }

    @ModelAttribute("phraseGroupName")
    List<PhraseGroupNameDto> getGroups(Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            return mapPhraseGroupToPhraseGroupNameDto(phraseGroupRepository.findByUser(user.get()));
        } else {
            logger.error("User not found");
            return Collections.emptyList();
        }
    }


    @PostMapping
    public String create(
            @ModelAttribute("phraseGroup") @Valid PhraseGroupWriteDto current,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                logger.error("{}: {}", fieldName, errorMessage);
            }
            return "redirect:/somethingWentWrong";
        }

        Optional<User> user = userRepository.findByEmail(authentication.getName());

        if (!file.isEmpty()) {
            if (!imageValidationService.isValidImage(file)) {
                return "redirect:/somethingWentWrong";
            }

            byte[] adjustedImage = imageProcessingService.adjustImage(file, 700, 500,"png");
            String imageUrl = cloudinaryService.uploadImage(adjustedImage);
            current.setImageUrl(imageUrl);
            phraseGroupRepository.save(mapPhraseGroupsToPhraseGroupWriteDto(null, user.get(), current));
        } else {
            phraseGroupRepository.save( mapPhraseGroupsToPhraseGroupWriteDto(null, user.get(), current));
        }
        return "redirect:/phraseGroup";
    }


    @DeleteMapping(params = "confirmDelete")
    String delete(@RequestParam("confirmDelete") int groupId) {
        Optional<PhraseGroup> phraseGroupOptional = phraseGroupRepository.findById(groupId);
        if (phraseGroupOptional.get().getImageUrl() != null) {
            String previousImageUrl = phraseGroupOptional.get().getImageUrl();
            cloudinaryService.deleteImage(previousImageUrl);
        }
        phraseGroupRepository.deleteById(groupId);
        return "redirect:/phraseGroup";
    }


}
