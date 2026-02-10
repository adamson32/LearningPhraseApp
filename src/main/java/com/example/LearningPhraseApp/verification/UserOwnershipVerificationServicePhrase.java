package com.example.LearningPhraseApp.verification;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.group.PhraseGroupRepository;
import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserOwnershipVerificationServicePhrase implements PhraseGroupMembershipService {
    private final PhraseGroupRepository phraseGroupRepository;
    private final UserRepository userRepository;

    UserOwnershipVerificationServicePhrase(PhraseGroupRepository phraseGroupRepository, UserRepository userRepository) {
        this.phraseGroupRepository = phraseGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isCurrentGroupPhrasesBelongsToUser(Authentication authentication, int groupID) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Optional<PhraseGroup> group = phraseGroupRepository.findById(groupID);

        return user.isPresent() && group.isPresent() &&
                group.get().getUser().equals(user.get());
    }
}
