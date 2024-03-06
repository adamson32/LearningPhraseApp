package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhrasesRepository extends JpaRepository<Phrases, Integer> {
    boolean existsByPhrase(String phrase);

    List<Phrases> findByGroupAndNextDateBefore(GroupPhrases groupPhrases, LocalDateTime currentDate);

    @Modifying
    @Query("UPDATE Phrases e SET e.progress = :progress, e.nextDate = :nextDate WHERE e.id = :id")
    void updatePhrasesByProgressAndNextDate(@Param("progress") int progress,
                                            @Param("nextDate") LocalDateTime nextDate, @Param("id") int id);

}
