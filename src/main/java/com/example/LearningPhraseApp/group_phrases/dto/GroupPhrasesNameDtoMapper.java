package com.example.LearningPhraseApp.group_phrases.dto;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.pharses.Phrases;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import com.example.LearningPhraseApp.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPhrasesNameDtoMapper {

    private GroupPhrasesNameDtoMapper() {
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
                    .image(group.getImage())
                    .user(user)
                    .build();
        } else {
            return GroupPhrases.builder()
                    .id(id)
                    .name(group.getName())
                    .description(group.getDescription())
                    .image(group.getImage())
                    .user(user)
                    .build();
        }

    }

    public static GroupPhrasesReadDto mapGroupPhrasesWriteDtoToGroupPhrases(int id, GroupPhrases group) {
        return GroupPhrasesReadDto.builder()
                .id(id)
                .name(group.getName())
                .description(group.getDescription())
                .image(group.getImage())
                .build();
    }

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
        System.out.println("I am in mapPhrase...");
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
