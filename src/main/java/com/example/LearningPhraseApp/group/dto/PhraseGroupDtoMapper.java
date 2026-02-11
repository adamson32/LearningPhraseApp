package com.example.LearningPhraseApp.group.dto;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class PhraseGroupDtoMapper {

    private PhraseGroupDtoMapper() {
    }

    public static List<PhraseGroupNameDto> mapPhraseGroupToPhraseGroupNameDto(List<PhraseGroup> group) {

        return group.stream()
                .map(phraseGroup -> new PhraseGroupNameDto(phraseGroup.getId(), phraseGroup.getName()))
                .collect(Collectors.toList());
    }

    public static PhraseGroup mapPhraseGroupsToPhraseGroupWriteDto(Integer id, User user, PhraseGroupWriteDto group) {
        if (id == null) {
            return PhraseGroup.builder()
                    .name(group.getName())
                    .description(group.getDescription())
                    .imageUrl(group.getImageUrl())
                    .user(user)
                    .build();
        } else {
            return PhraseGroup.builder()
                    .id(id)
                    .name(group.getName())
                    .description(group.getDescription())
                    .imageUrl(group.getImageUrl())
                    .user(user)
                    .build();
        }

    }

    public static PhraseGroupReadDto mapPhraseGroupWriteDtoToPhraseGroup(int id, PhraseGroup group) {
        return PhraseGroupReadDto.builder()
                .id(id)
                .name(group.getName())
                .description(group.getDescription())
                .imageUrl(group.getImageUrl())
                .build();
    }


}
