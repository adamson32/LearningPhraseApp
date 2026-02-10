package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;

import java.util.List;

public interface PhraseGroupService {
    List<PhrasesReadDTO> readAllPhrasesFromGroupById(int id);
}
