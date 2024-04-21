package com.example.LearningPhraseApp.verification;

import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.group_phrases.GroupPhrasesRepository;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserOwnershipVerificationServicePhrases implements GroupPhrasesMembershipService {
    private final GroupPhrasesRepository groupPhrasesRepository;
    private final UserRepository userRepository;

    UserOwnershipVerificationServicePhrases(GroupPhrasesRepository groupPhrasesRepository, UserRepository userRepository) {
        this.groupPhrasesRepository = groupPhrasesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isCurrentGroupPhrasesBelongsToUser(Authentication authentication, int groupID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Optional<GroupPhrases> group = groupPhrasesRepository.findById(groupID);

        return user.isPresent() && group.isPresent() &&
                group.get().getUser().equals(user.get());
    }
}
