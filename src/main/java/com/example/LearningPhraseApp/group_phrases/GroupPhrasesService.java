package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;

import java.util.List;

public interface GroupPhrasesService {
    List<PhrasesReadDTO> readAllPhrasesFromGroupById(int id);
}
