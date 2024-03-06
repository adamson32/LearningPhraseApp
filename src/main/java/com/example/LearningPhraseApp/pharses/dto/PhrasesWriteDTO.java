package com.example.LearningPhraseApp.pharses.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.pharses.Phrases;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PhrasesWriteDTO {

    @NotEmpty(message = "phrase must be not empty")
    private String phrase;
    private String description;
    @NotEmpty(message = "meaning must be not empty")
    private String meaning;
    private GroupPhrases group;

    public PhrasesWriteDTO() {
    }

    public Phrases toPhrases(GroupPhrases group) {

        var result = new Phrases();
        result.setPhrase(phrase);
        result.setDescription(description);
        result.setMeaning(meaning);
        if (group != null) result.setGroup(group);
        return result;
    }
}
