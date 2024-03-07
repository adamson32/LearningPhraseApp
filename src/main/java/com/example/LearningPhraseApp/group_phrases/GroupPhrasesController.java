package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesNameDto;
import com.example.LearningPhraseApp.group_phrases.dto.GroupPhrasesWriteDto;
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
    private final GroupPhrasesService groupPhrasesService;
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(GroupPhrasesController.class);


    public GroupPhrasesController(GroupPhrasesRepository groupPhrasesRepository, GroupPhrasesService groupPhrasesService, UserRepository userRepository) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.groupPhrasesService = groupPhrasesService;
        this.userRepository = userRepository;
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
            return mapGroupPhrasesToGroupPhrasesNameDto(groupPhrasesService.getGroupPhrasesByUser(user.get()));
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
                System.out.println(fieldName + errorMessage);

            }
            return "groupPhrases";
        }
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        if (!file.isEmpty()) {
            groupPhrasesService.setImageFileAndSave(current, file);
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(null,user.get(),current));
        } else {
            groupPhrasesRepository.save(mapGroupPhrasesToGroupPhrasesWriteDto(null,user.get(),current));
        }
        return "redirect:/groupPhrases";
    }



    @DeleteMapping(params = "confirmDelete")
    String deleteGroup(@ModelAttribute("groupPhrasesName") GroupPhrasesNameDto current,
                       @RequestParam("confirmDelete") int index,
                       Model model) {
        groupPhrasesRepository.deleteById(index);
        model.addAttribute("groupPhrases", new GroupPhrases());

        return "redirect:/groupPhrases";
    }


}
