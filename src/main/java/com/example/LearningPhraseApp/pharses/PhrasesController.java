package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group.PhraseGroupService;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import com.example.LearningPhraseApp.pharses.dto.PhrasesWriteDTO;
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

import java.util.List;


@Controller
@RequestMapping("/phrases")
public class PhrasesController {
    private final PhrasesRepository repository;
    private final PhraseGroupService phraseGroupService;
    private final PhraseGroupMembershipService phraseGroupMembershipService;
    private final PhrasesService phrasesService;
    private int storedGroupId;
    private static final Logger logger = LoggerFactory.getLogger(PhrasesController.class);

    public PhrasesController(PhrasesRepository repository, PhraseGroupService phraseGroupService, PhraseGroupMembershipService phraseGroupMembershipService, PhrasesService phrasesService) {
        this.repository = repository;
        this.phraseGroupService = phraseGroupService;
        this.phraseGroupMembershipService = phraseGroupMembershipService;
        this.phrasesService = phrasesService;
    }

    @GetMapping
    String showTemplate(@RequestParam("group") int groupID,
                        Model model, Authentication authentication) {
        if (phraseGroupMembershipService.isCurrentPhraseGroupBelongsToUser(authentication, groupID)) {
            storedGroupId = groupID;
            model.addAttribute("phrases", getPhrases(groupID));
            model.addAttribute("phrase", new PhrasesWriteDTO());

            return "phrases";
        }
        return "redirect:/noAccess";

    }

    private List<PhrasesReadDTO> getPhrases(int groupID) {
        return phraseGroupService.readAllPhrasesFromGroupById(groupID);
    }

    @DeleteMapping(params = "confirmDelete")
    String delete(
            @RequestParam("confirmDelete") int index,
            Model model) {
        repository.deleteById(index);
        model.addAttribute("group", storedGroupId);
        return "redirect:/phrases?group=" + storedGroupId;
    }

    @PutMapping(params = "updatePhrase")
    String updatePhrase(@RequestParam("updatePhrase") int index,
                        @ModelAttribute("phrase") @Valid PhrasesWriteDTO toUpdate,
                        Model model,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String errorMessage = error.getDefaultMessage();
                logger.error(errorMessage);
            }
            return "redirect:/somethingWentWrong";
        }

        phrasesService.updatePhrase(toUpdate, index);
        model.addAttribute("group", storedGroupId);
        return "redirect:/phrases?group=" + storedGroupId;

    }

    @PostMapping
    String createPhrases(@ModelAttribute("phrase") @Valid PhrasesWriteDTO current,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                String errorMessage = error.getDefaultMessage();
                logger.error(errorMessage);
            }
            return "redirect:/somethingWentWrong";
        }

        phrasesService.createPhrase(current, storedGroupId);
        return "redirect:/phrases?group=" + storedGroupId;
    }
}
