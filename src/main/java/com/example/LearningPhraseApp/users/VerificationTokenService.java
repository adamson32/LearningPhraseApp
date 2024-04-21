package com.example.LearningPhraseApp.users;

import com.example.LearningPhraseApp.verificationToken.VerificationToken;

public interface VerificationTokenService {
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String token);
}
