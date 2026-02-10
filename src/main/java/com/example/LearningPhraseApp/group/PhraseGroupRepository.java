package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhraseGroupRepository extends JpaRepository<PhraseGroup, Integer> {

    List<PhraseGroup> findByUser(User user);

}
