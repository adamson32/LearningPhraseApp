package com.example.LearningPhraseApp.group.dto;

import lombok.Builder;

@Builder
public record PhraseGroupReadDto(
        int id,
        String name,
        String description,
        String imageUrl
) {

}
