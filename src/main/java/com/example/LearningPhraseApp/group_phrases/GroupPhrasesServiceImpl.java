package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.group_phrases.dto.GroupReadModel;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupPhrasesServiceImpl implements GroupPhrasesService {
    private final GroupPhrasesRepository groupPhrasesRepository;

    GroupPhrasesServiceImpl(GroupPhrasesRepository groupPhrasesRepository) {
        this.groupPhrasesRepository = groupPhrasesRepository;
    }

    @Override
    public List<PhrasesReadDTO> readAllPhrasesFromGroupById(int id) {
        Optional<GroupPhrases> groupPhrasesOptional = groupPhrasesRepository.findById(id);

        if (groupPhrasesOptional.isPresent()) {
            return groupPhrasesOptional
                    .map(GroupReadModel::new)
                    .map(GroupReadModel::getPhrases)
                    .orElseThrow(() -> new EntityNotFoundException("No phrases found for group with id " + id));
        } else {
            throw new EntityNotFoundException("Group with id " + id + " not found");
        }
    }
}
