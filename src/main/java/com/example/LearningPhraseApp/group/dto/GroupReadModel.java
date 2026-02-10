package com.example.LearningPhraseApp.group.dto;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GroupReadModel {
    List<PhrasesReadDTO> phrasesReadDto;
    public GroupReadModel(PhraseGroup source) {
        phrasesReadDto = source.getPhrases().stream()
                .map(PhrasesReadDTO::new)
                .collect(Collectors.toList());
    }

    public List<PhrasesReadDTO> getPhrases() {
        return phrasesReadDto;
    }

    public void setPhrases(List<PhrasesReadDTO> phrases) {
        this.phrasesReadDto = phrases;
    }

}
