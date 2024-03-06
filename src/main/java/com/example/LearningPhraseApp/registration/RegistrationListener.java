package com.example.LearningPhraseApp.registration;

import com.example.LearningPhraseApp.users.User;
import com.example.LearningPhraseApp.users.UserRepository;
import com.example.LearningPhraseApp.users.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    public RegistrationListener(UserService userService, MessageSource messages, UserRepository userRepository,
                                JavaMailSender mailSender) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private final UserService userService;
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registration/registrationConfirm?token=" + token;
        try {
            String message = "Please confirm email ";
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setFrom("dziakiewiczadam@gmail.com");
            email.setSubject(subject);
            email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
            mailSender.send(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
