package com.example.LearningPhraseApp.users;

import com.example.LearningPhraseApp.verificationToken.VerificationToken;
import com.example.LearningPhraseApp.verificationToken.VerificationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultVerificationTokenService implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public DefaultVerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}

