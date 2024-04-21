package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.group_phrases.GroupPhrasesRepository;
import com.example.LearningPhraseApp.verification.GroupPhrasesMembershipService;
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
    private final GroupPhrasesRepository groupPhrasesRepository;
    private final GroupPhrasesMembershipService groupPhrasesMembershipService;

    private final PhrasesService phrasesService;
    private int storedGroupId;
    LocalDateTime localDateTime;


    public StartPhrasesController(PhrasesRepository phrasesRepository, GroupPhrasesRepository groupPhrasesRepository, GroupPhrasesMembershipService groupPhrasesMembershipService, PhrasesService phrasesService) {
        this.phrasesRepository = phrasesRepository;
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.groupPhrasesMembershipService = groupPhrasesMembershipService;
        this.phrasesService = phrasesService;
    }

    @GetMapping(params = "group")
    String showTemplate(@RequestParam("group") int groupId, Model model, Authentication authentication) {
        if (groupPhrasesMembershipService.isCurrentGroupPhrasesBelongsToUser(authentication, groupId)) {
            GroupPhrases gr = groupPhrasesRepository.findById(groupId).get();
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
        return "redirect:/groupPhrasesView?group=" + storedGroupId;

    }

}
