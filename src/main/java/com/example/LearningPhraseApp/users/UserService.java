package com.example.LearningPhraseApp.users;

public interface UserService {
    User registerNewUserAccount(UserRegistrationDto userDto);
    void saveRegisteredUser(User user);
}