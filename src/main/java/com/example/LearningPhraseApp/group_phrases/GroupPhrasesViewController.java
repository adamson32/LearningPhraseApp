package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.cloudinary.CloudinaryService;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesReadDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesWriteDto;
import com.example.LearningPhraseApp.image.ImageProcessingService;
import com.example.LearningPhraseApp.image.ImageValidationService;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import com.example.LearningPhraseApp.verification.GroupPhrasesMembershipService;
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

import java.util.List;
import java.util.Optional;

import static com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesDtoMapper.mapGroupPhrasesToGroupPhrasesWriteDto;
import static com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesDtoMapper.mapGroupPhrasesWriteDtoToGroupPhrases;

@Controller
@RequestMapping("/groupPhrasesView")
class GroupPhrasesViewController {

    private static final Logger logger = LoggerFactory.getLogger(GroupPhrasesViewController.class);

    private final GroupPhrasesRepository groupPhrasesRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageProcessingService imageProcessingService;
    private final ImageValidationService imageValidationService;
    private final GroupPhrasesMembershipService groupPhrasesMembershipService;


    GroupPhrasesViewController(GroupPhrasesRepository groupPhrasesRepository, UserRepository userRepository,
                               CloudinaryService cloudinaryService, ImageProcessingService imageProcessingService,
                               ImageValidationService imageValidationService,
                               GroupPhrasesMembershipService groupPhrasesMembershipService) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageProcessingService = imageProcessingService;
        this.imageValidationService = imageValidationService;
        this.groupPhrasesMembershipService = groupPhrasesMembershipService;
    }

    @GetMapping
    public String showGroupView(@RequestParam("group") int groupID, Model model,
                                Authentication authentication) {
        if (groupPhrasesMembershipService.isCurrentGroupPhrasesBelongsToUser(authentication, groupID)) {
            Optional<GroupPhrases> groupPhrasesOptional = groupPhrasesRepository.findById(groupID);
            GroupPhrasesReadDto group = mapGroupPhrasesWriteDtoToGroupPhrases(groupID,
                    groupPhrasesOptional.get());
            model.addAttribute("groupView", group);
            return "groupPhrasesView";
        }
        return "redirect:/noAccess";
    }


    @DeleteMapping(params = "confirmDelete")
    String delete(@RequestParam("confirmDelete") int groupId
    ) {
        deleteImageUrl(groupId);
        groupPhrasesRepository.deleteById(groupId);
        return "redirect:/groupPhrases";
    }

    private void deleteImageUrl(int groupId) {
        Optional<GroupPhrases> groupPhrasesOptional = groupPhrasesRepository.findById(groupId);
        if (groupPhrasesOptional.get().getImageUrl() != null) {
            String previousImageUrl = groupPhrasesOptional.get().getImageUrl();
            cloudinaryService.deleteImage(previousImageUrl);
        }
    }

    @PutMapping("/{groupId}")
    public String modify(
            @PathVariable("groupId") int groupId,
            @ModelAttribute("groupView") @Valid GroupPhrasesWriteDto toUpdate,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String errorMessage = error.getDefaultMessage();
                logger.error(errorMessage);
            }
            return "redirect:/somethingWentWrong";
        }
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        deleteImageUrl(groupId);
        if (!file.isEmpty()) {
            if (!imageValidationService.isValidImage(file)) {
                return "redirect:/somethingWentWrong";
            }
            byte[] resizeImage = imageProcessingService.adjustImage(file, 700, 500,"png");
            String imageUrl = cloudinaryService.uploadImage(resizeImage);
            toUpdate.setImageUrl(imageUrl);
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(groupId, user.get(), toUpdate));

        } else {
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(groupId, user.get(), toUpdate));
        }
        return "redirect:/groupPhrasesView?group=" + groupId;
    }


}
