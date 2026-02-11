package com.example.LearningPhraseApp.verification;

import org.springframework.security.core.Authentication;

public interface PhraseGroupMembershipService {
    boolean isCurrentPhraseGroupBelongsToUser(Authentication authentication, int groupID);
}
