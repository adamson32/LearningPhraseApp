package com.example.LearningPhraseApp.group_phrases.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GroupReadModel {
    List<PhrasesReadDTO> phrasesReadDto;
    public GroupReadModel(GroupPhrases source) {
        phrasesReadDto = source.getEnglishPhrases().stream()
                .map(PhrasesReadDTO::new)
                .collect(Collectors.toList());
    }

    public List<PhrasesReadDTO> getPhrases() {
        return phrasesReadDto;
    }

    public void setPhrases(List<PhrasesReadDTO> englishPhrases) {
        this.phrasesReadDto = englishPhrases;
    }

}
