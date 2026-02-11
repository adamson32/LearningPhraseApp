package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.group.PhraseGroupRepository;
import com.example.LearningPhraseApp.verification.PhraseGroupMembershipService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.LearningPhraseApp.pharses.dto.PhrasesDtoMapper.mapPhrasesToPhrasesReadDto;


@Controller
@RequestMapping("/startPhrases")
public class StartPhrasesController {
    private final PhrasesRepository phrasesRepository;
    private final PhraseGroupRepository phraseGroupRepository;
    private final PhraseGroupMembershipService phraseGroupMembershipService;

    private final PhrasesService phrasesService;
    private int storedGroupId;
    LocalDateTime localDateTime;


    public StartPhrasesController(PhrasesRepository phrasesRepository, PhraseGroupRepository phraseGroupRepository, PhraseGroupMembershipService phraseGroupMembershipService, PhrasesService phrasesService) {
        this.phrasesRepository = phrasesRepository;
        this.phraseGroupRepository = phraseGroupRepository;
        this.phraseGroupMembershipService = phraseGroupMembershipService;
        this.phrasesService = phrasesService;
    }

    @GetMapping(params = "group")
    String showTemplate(@RequestParam("group") int groupId, Model model, Authentication authentication) {
        if (phraseGroupMembershipService.isCurrentPhraseGroupBelongsToUser(authentication, groupId)) {
            PhraseGroup gr = phraseGroupRepository.findById(groupId).get();
            storedGroupId = groupId;
            localDateTime = LocalDateTime.now();
            model.addAttribute("phrasesByNextDate", mapPhrasesToPhrasesReadDto(phrasesRepository.findByGroupAndNextDateBefore(gr, localDateTime)));
            return "startPhrases";

        }
        return "redirect:/noAccess";
    }

    @PutMapping()
    String updateAttemptAndNextDateByDay(
            Model model, @RequestParam("phraseId") List<Integer> phraseId,
            @RequestParam("plusDays") List<Integer> plusDaysList
    ) {
        phrasesService.updateAttemptAndNextDateByDay(phraseId, plusDaysList);
        model.addAttribute("group", storedGroupId);
        return "redirect:/phraseGroupView?group=" + storedGroupId;

    }

}
