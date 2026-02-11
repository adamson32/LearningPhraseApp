package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.group.PhraseGroupRepository;
import com.example.LearningPhraseApp.pharses.dto.PhrasesReadDTO;
import com.example.LearningPhraseApp.pharses.dto.PhrasesWriteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.LearningPhraseApp.pharses.dto.PhrasesDtoMapper.mapPhrasesReadDtoToPhrases;
import static com.example.LearningPhraseApp.pharses.dto.PhrasesDtoMapper.mapPhrasesToPhrasesReadDto;


@Service
class DefaultPhrasesService implements PhrasesService {
    private final PhrasesRepository repository;
    private final PhraseGroupRepository phraseGroupRepository;
    private static final Logger logger = LoggerFactory.getLogger(DefaultPhrasesService.class);

    DefaultPhrasesService(PhrasesRepository repository, PhraseGroupRepository phraseGroupRepository) {
        this.repository = repository;
        this.phraseGroupRepository = phraseGroupRepository;
    }

    @Override
    public void createPhrase(final PhrasesWriteDTO source, int groupId) {

        Optional<PhraseGroup> groupOptional = phraseGroupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            PhraseGroup group = groupOptional.get();
            repository.save(source.toPhrases(group));
        } else {
            logger.warn("Group {} not found", groupId);
        }

    }

    @Override
    public void updatePhrase(final PhrasesWriteDTO source, int id) {

        Optional<Phrases> phrasesOptional = repository.findById(id);
        if (phrasesOptional.isPresent()) {
            Phrases phrases = phrasesOptional.get();
            phrases.updateFrom(source.toPhrases(null));
            repository.save(phrases);
        } else {
            logger.warn("Phrase {} not found", id);
        }

    }

    @Override
    public void updateAttemptAndNextDateByDay(List<Integer> phraseId, List<Integer> plusDaysList) {
        List<PhrasesReadDTO> phrasesToUpdate = mapPhrasesToPhrasesReadDto(repository.findAllById(phraseId));
        LocalDateTime localDateTime = LocalDateTime.now();
        for (int i = 0; i < phrasesToUpdate.size(); i++){
            PhrasesReadDTO currentPhrase = phrasesToUpdate.get(i);

            if(plusDaysList.get(i)==0 && currentPhrase.getProgress() == 1){ // click 1day when progress == 1
                currentPhrase.setNextDate(localDateTime.plusDays(1));
            }
            else if(plusDaysList.get(i) == 0 && currentPhrase.getProgress() > 1){ // click 1 day when progress > 1
                currentPhrase.setProgress(currentPhrase.getProgress() - 1);
                currentPhrase.setNextDate(localDateTime.plusDays(1));
            }
            else { // click other buttons
                currentPhrase.setProgress(currentPhrase.getProgress() + 1);
                currentPhrase.setNextDate(localDateTime.plusDays(plusDaysList.get(i)));
            }
            //repository.updatePhrasesByProgressAndNextDate(currentPhrase.getProgress(), currentPhrase.getNextDate(), currentPhrase.getId());
        }
        repository.saveAll(mapPhrasesReadDtoToPhrases(phrasesToUpdate));
    }


}
