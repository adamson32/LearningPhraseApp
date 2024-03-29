package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPhrasesRepository extends JpaRepository<GroupPhrases, Integer> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE group_english_phrases " +
            "SET next_date = (" +
            "    SELECT MIN(ep.next_date) " +
            "    FROM english_phrases ep " +
            "    WHERE ep.group_eng_phrs_id = group_english_phrases.id " +
            ")")
    void updateGroupEnglishPhrasesByNextDate();// nulle były w bazie


    @Query(value = "SELECT gp FROM GroupPhrases gp")
    List<GroupPhrases> findAllName();

    List<GroupPhrases> findByUser(User user);

}
