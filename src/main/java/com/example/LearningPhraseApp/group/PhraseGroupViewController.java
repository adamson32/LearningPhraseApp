package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.cloudinary.CloudinaryService;
import com.example.LearningPhraseApp.group.dto.PhraseGroupReadDto;
import com.example.LearningPhraseApp.group.dto.PhraseGroupWriteDto;
import com.example.LearningPhraseApp.image.ImageProcessingService;
import com.example.LearningPhraseApp.image.ImageValidationService;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import com.example.LearningPhraseApp.verification.PhraseGroupMembershipService;
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

import static com.example.LearningPhraseApp.group.dto.PhraseGroupDtoMapper.*;

@Controller
@RequestMapping("/phraseGroupView")
class PhraseGroupViewController {

    private static final Logger logger = LoggerFactory.getLogger(PhraseGroupViewController.class);

    private final PhraseGroupRepository phraseGroupRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageProcessingService imageProcessingService;
    private final ImageValidationService imageValidationService;
    private final PhraseGroupMembershipService phraseGroupMembershipService;


    PhraseGroupViewController(PhraseGroupRepository phraseGroupRepository, UserRepository userRepository,
                              CloudinaryService cloudinaryService, ImageProcessingService imageProcessingService,
                              ImageValidationService imageValidationService,
                              PhraseGroupMembershipService phraseGroupMembershipService) {
        this.phraseGroupRepository = phraseGroupRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageProcessingService = imageProcessingService;
        this.imageValidationService = imageValidationService;
        this.phraseGroupMembershipService = phraseGroupMembershipService;
    }

    @GetMapping
    public String showGroupView(@RequestParam("group") int groupID, Model model,
                                Authentication authentication) {
        if (phraseGroupMembershipService.isCurrentPhraseGroupBelongsToUser(authentication, groupID)) {
            Optional<PhraseGroup> phrasesGroupOptional = phraseGroupRepository.findById(groupID);
            PhraseGroupReadDto group = mapPhraseGroupWriteDtoToPhraseGroup(groupID,
                    phrasesGroupOptional.get());
            model.addAttribute("groupView", group);
            return "phraseGroupView";
        }
        return "redirect:/noAccess";
    }


    @DeleteMapping(params = "confirmDelete")
    String delete(@RequestParam("confirmDelete") int groupId
    ) {
        deleteImageUrl(groupId);
        phraseGroupRepository.deleteById(groupId);
        return "redirect:/phraseGroup";
    }

    private void deleteImageUrl(int groupId) {
        Optional<PhraseGroup> phraseGroupOptional = phraseGroupRepository.findById(groupId);
        if (phraseGroupOptional.get().getImageUrl() != null) {
            String previousImageUrl = phraseGroupOptional.get().getImageUrl();
            cloudinaryService.deleteImage(previousImageUrl);
        }
    }

    @PutMapping("/{groupId}")
    public String modify(
            @PathVariable("groupId") int groupId,
            @ModelAttribute("groupView") @Valid PhraseGroupWriteDto toUpdate,
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
            phraseGroupRepository.save(mapPhraseGroupsToPhraseGroupWriteDto(groupId, user.get(), toUpdate));

        } else {
            phraseGroupRepository.save(mapPhraseGroupsToPhraseGroupWriteDto(groupId, user.get(), toUpdate));
        }
        return "redirect:/phraseGroupView?group=" + groupId;
    }


}
