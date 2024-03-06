package com.example.LearningPhraseApp.group_phrases.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GroupReadModel {
    List<PhrasesReadDTO> englishPhrases;
    public GroupReadModel(GroupPhrases source) {
        englishPhrases = source.getEnglishPhrases().stream()
                .map(PhrasesReadDTO::new)
                .collect(Collectors.toList());
    }

    public List<PhrasesReadDTO> getPhrases() {
        return englishPhrases;
    }

    public void setPhrases(List<PhrasesReadDTO> englishPhrases) {
        this.englishPhrases = englishPhrases;
    }

}
