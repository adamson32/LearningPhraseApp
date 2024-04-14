package com.example.LearningPhraseApp.group_phrases.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class GroupPhrasesDtoMapper {

    private GroupPhrasesDtoMapper() {
    }

    public static List<GroupPhrasesNameDto> mapGroupPhrasesToGroupPhrasesNameDto(List<GroupPhrases> group) {

        return group.stream()
                .map(groupPhrases -> new GroupPhrasesNameDto(groupPhrases.getId(), groupPhrases.getName()))
                .collect(Collectors.toList());
    }

    public static GroupPhrases mapGroupPhrasesToGroupPhrasesWriteDto(Integer id, User user, GroupPhrasesWriteDto group) {
        if (id == null) {
            return GroupPhrases.builder()
                    .name(group.getName())
                    .description(group.getDescription())
                    .imageUrl(group.getImageUrl())
                    .user(user)
                    .build();
        } else {
            return GroupPhrases.builder()
                    .id(id)
                    .name(group.getName())
                    .description(group.getDescription())
                    .imageUrl(group.getImageUrl())
                    .user(user)
                    .build();
        }

    }

    public static GroupPhrasesReadDto mapGroupPhrasesWriteDtoToGroupPhrases(int id, GroupPhrases group) {
        return GroupPhrasesReadDto.builder()
                .id(id)
                .name(group.getName())
                .description(group.getDescription())
                .imageUrl(group.getImageUrl())
                .build();
    }


}
