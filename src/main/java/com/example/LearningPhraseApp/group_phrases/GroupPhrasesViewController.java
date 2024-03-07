package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesReadDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesWriteDto;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.Pair;
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

@Controller
@RequestMapping("/groupPhrasesView")
class GroupPhrasesViewController {

    private static final Logger logger = LoggerFactory.getLogger(GroupPhrasesViewController.class);

    private final GroupPhrasesRepository groupPhrasesRepository;
    private final GroupPhrasesService groupPhrasesService;
    private final UserRepository userRepository;

    GroupPhrasesViewController(GroupPhrasesRepository groupPhrasesRepository, GroupPhrasesService groupPhrasesService, UserRepository userRepository) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.groupPhrasesService = groupPhrasesService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showGroupDetails(@RequestParam("group") int groupID, Model model,
                                   Authentication authentication) {
        if (groupPhrasesService.isCurrentGroupBelongsToUser(authentication, groupID))
        {
            GroupPhrasesReadDto group;
            String base64Image;
            Pair<String, GroupPhrasesReadDto> result = groupPhrasesService.getImageAndGroupById(groupID);
            if (result != null) {
                base64Image = result.getLeft();
                group = result.getRight();

            } else {
                logger.info("GroupPhrases with id {} not found", groupID);
                return "redirect:/somethingWentWrong";
            }
            model.addAttribute("base64Image", base64Image);
            model.addAttribute("groupView", group);

            return "groupPhrasesView";
        }
        return "redirect:/noAccess";
    }

    @DeleteMapping(params = "confirmDelete")
    String delete(@RequestParam("confirmDelete") int index
    ){
            groupPhrasesRepository.deleteById(index);
        return "redirect:/groupPhrases";
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

        if (!file.isEmpty()) {
            groupPhrasesService.setImageFileAndSave(toUpdate, file);
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(groupId,user.get(),toUpdate));

        } else   {
            groupPhrasesService.updatePost(mapGroupPhrasesToGroupPhrasesWriteDto(groupId,user.get(),toUpdate));
        }
        return "redirect:/groupPhrasesView?group="+groupId ;
    }

}
