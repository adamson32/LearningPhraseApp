package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.group_phrases.GroupPhrasesRepository;
import com.example.LearningPhraseApp.pharses.dto.PhrasesWriteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
class PhrasesService {
    private final PhrasesRepository repository;
    private final GroupPhrasesRepository groupPhrasesRepository;
    private static final Logger logger = LoggerFactory.getLogger(PhrasesService.class);

    PhrasesService(PhrasesRepository repository, GroupPhrasesRepository groupPhrasesRepository) {
        this.repository = repository;
        this.groupPhrasesRepository = groupPhrasesRepository;
    }

    public void createPhrase(final PhrasesWriteDTO source, int groupId) {

        Optional<GroupPhrases> groupOptional = groupPhrasesRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            GroupPhrases group = groupOptional.get();
            repository.save(source.toPhrases(group));
        } else {
            logger.warn("Group {} not found", groupId);
        }

    }

    public void updatePhrase(final PhrasesWriteDTO source, int id) {

        Optional<Phrases> englishPhrasesOptional = repository.findById(id);
        if (englishPhrasesOptional.isPresent()) {
            Phrases englishPhrases = englishPhrasesOptional.get();
            englishPhrases.updateFrom(source.toPhrases(null));
            repository.save(englishPhrases);
        } else {
            logger.warn("Phrase {} not found", id);
        }

    }


}
