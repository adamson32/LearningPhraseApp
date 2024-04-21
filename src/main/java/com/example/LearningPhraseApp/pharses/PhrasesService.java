package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.pharses.dto.PhrasesWriteDTO;

import java.util.List;

public interface PhrasesService {
    void createPhrase(final PhrasesWriteDTO source, int groupId);
    void updatePhrase(final PhrasesWriteDTO source, int id);
    void updateAttemptAndNextDateByDay(List<Integer> phraseId, List<Integer> plusDaysList);
}
