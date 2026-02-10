package com.example.LearningPhraseApp.verification;

import org.springframework.security.core.Authentication;

public interface PhraseGroupMembershipService {
    boolean isCurrentGroupPhrasesBelongsToUser(Authentication authentication, int groupID);
}
