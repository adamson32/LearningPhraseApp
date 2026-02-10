package com.example.LearningPhraseApp.group.dto;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class PhraseGroupDtoMapper {

    private PhraseGroupDtoMapper() {
    }

    public static List<PhraseGroupNameDto> mapGroupPhrasesToGroupPhrasesNameDto(List<PhraseGroup> group) {

        return group.stream()
                .map(groupPhrases -> new PhraseGroupNameDto(groupPhrases.getId(), groupPhrases.getName()))
                .collect(Collectors.toList());
    }

    public static PhraseGroup mapGroupPhrasesToGroupPhrasesWriteDto(Integer id, User user, PhraseGroupWriteDto group) {
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

    public static PhraseGroupReadDto mapGroupPhrasesWriteDtoToGroupPhrases(int id, PhraseGroup group) {
        return PhraseGroupReadDto.builder()
                .id(id)
                .name(group.getName())
                .description(group.getDescription())
                .imageUrl(group.getImageUrl())
                .build();
    }


}
