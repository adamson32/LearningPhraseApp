package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.cloudinary.CloudinaryService;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesNameDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesWriteDto;
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

import static com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesDtoMapper.mapGroupPhrasesToGroupPhrasesNameDto;
import static com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesDtoMapper.mapGroupPhrasesToGroupPhrasesWriteDto;

@Controller
@RequestMapping("/groupPhrases")
public class GroupPhrasesController {

    private final GroupPhrasesRepository groupPhrasesRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageProcessingService imageProcessingService;
    private final ImageValidationService imageValidationService;
    Logger logger = LoggerFactory.getLogger(GroupPhrasesController.class);


    public GroupPhrasesController(GroupPhrasesRepository groupPhrasesRepository,
                                  UserRepository userRepository, CloudinaryService cloudinaryService,
                                  ImageProcessingService imageProcessingService,
                                  ImageValidationService imageValidationService) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageProcessingService = imageProcessingService;
        this.imageValidationService = imageValidationService;
    }

    @GetMapping()
    String showTemplate(Model model) {
        model.addAttribute("groupPhrases", new GroupPhrasesWriteDto());
        return "groupPhrases";
    }

    @ModelAttribute("groupPhrasesName")
    List<GroupPhrasesNameDto> getGroups(Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            return mapGroupPhrasesToGroupPhrasesNameDto(groupPhrasesRepository.findByUser(user.get()));
        } else {
            logger.error("User not found");
            return Collections.emptyList();
        }
    }


    @PostMapping
    public String addDataToDatabase(
            @ModelAttribute("groupPhrases") @Valid GroupPhrasesWriteDto current,
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
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(null, user.get(), current));
        } else {
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(null, user.get(), current));
        }
        return "redirect:/groupPhrases";
    }


    @DeleteMapping(params = "confirmDelete")
    String deleteGroup(@RequestParam("confirmDelete") int groupId) {
        Optional<GroupPhrases> groupPhrasesOptional = groupPhrasesRepository.findById(groupId);
        if (groupPhrasesOptional.get().getImageUrl() != null) {
            String previousImageUrl = groupPhrasesOptional.get().getImageUrl();
            cloudinaryService.deleteImage(previousImageUrl);
        }
        groupPhrasesRepository.deleteById(groupId);
        return "redirect:/groupPhrases";
    }


}
