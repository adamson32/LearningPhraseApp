package com.example.LearningPhraseApp.group_phrases;

import com.example.LearningPhraseApp.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPhrasesRepository extends JpaRepository<GroupPhrases, Integer> {

    List<GroupPhrases> findByUser(User user);

}
