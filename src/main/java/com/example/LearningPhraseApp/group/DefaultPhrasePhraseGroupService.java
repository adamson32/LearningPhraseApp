package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.group.dto.GroupReadModel;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultPhrasePhraseGroupService implements PhraseGroupService {
    private final PhraseGroupRepository phraseGroupRepository;

    DefaultPhrasePhraseGroupService(PhraseGroupRepository phraseGroupRepository) {
        this.phraseGroupRepository = phraseGroupRepository;
    }

    @Override
    public List<PhrasesReadDTO> readAllPhrasesFromGroupById(int id) {
        Optional<PhraseGroup> groupPhrasesOptional = phraseGroupRepository.findById(id);

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
