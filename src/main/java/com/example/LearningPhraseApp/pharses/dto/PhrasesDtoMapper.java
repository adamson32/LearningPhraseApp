package com.example.LearningPhraseApp.pharses.dto;

import com.example.LearningPhraseApp.pharses.Phrases;

import java.util.ArrayList;
import java.util.List;

public class PhrasesDtoMapper {

    public static List<PhrasesReadDTO> mapPhrasesToPhrasesReadDto(List<Phrases> allById) {
        List<PhrasesReadDTO> phrasesReadDtoList = new ArrayList<>();

        for (Phrases phrases : allById) {
            PhrasesReadDTO phrasesReadDto = PhrasesReadDTO.builder()
                    .id(phrases.getId())
                    .phrase(phrases.getPhrase())
                    .description(phrases.getDescription())
                    .meaning(phrases.getMeaning())
                    .nextDate(phrases.getNextDate())
                    .progress(phrases.getProgress())
                    .group(phrases.getGroup())
                    .build();

            phrasesReadDtoList.add(phrasesReadDto);
        }

        return phrasesReadDtoList;
    }


    public static List<Phrases> mapPhrasesReadDtoToPhrases(List<PhrasesReadDTO> allById) {
        List<Phrases> phrasesList = new ArrayList<>();

        for (PhrasesReadDTO phrasesReadDto : allById) {
            Phrases phrase = Phrases.builder()
                    .id(phrasesReadDto.getId())
                    .phrase(phrasesReadDto.getPhrase())
                    .description(phrasesReadDto.getDescription())
                    .meaning(phrasesReadDto.getMeaning())
                    .progress(phrasesReadDto.getProgress())
                    .nextDate(phrasesReadDto.getNextDate())
                    .group(phrasesReadDto.getGroup())
                    .build();
            phrasesList.add(phrase);
        }

        return phrasesList;
    }
}
