package com.example.LearningPhraseApp.group_phrases.dto;

import lombok.Builder;

@Builder
public record GroupPhrasesReadDto(
        int id,
        String name,
        String description,
        byte[] image
) {

}
