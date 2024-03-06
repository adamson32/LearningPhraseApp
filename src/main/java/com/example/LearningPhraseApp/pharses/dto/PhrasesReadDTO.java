package com.example.LearningPhraseApp.pharses.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.pharses.Phrases;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhrasesReadDTO {

    private int id;
    private String phrase;
    private String description;
    private String meaning;
    private LocalDateTime nextDate;
    private int progress;
    private GroupPhrases group;


    public PhrasesReadDTO(Phrases source) {
        id = source.getId();
        phrase = source.getPhrase();
        meaning = source.getMeaning();
        description = source.getDescription();
        nextDate = source.getNextDate();
    }


}
