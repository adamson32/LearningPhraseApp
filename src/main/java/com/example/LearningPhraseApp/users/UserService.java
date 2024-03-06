package com.example.LearningPhraseApp.users;

import com.example.LearningPhraseApp.roles.Role;
import com.example.LearningPhraseApp.roles.RoleEnum;
import com.example.LearningPhraseApp.roles.RoleRepository;
import com.example.LearningPhraseApp.verificationToken.VerificationToken;
import com.example.LearningPhraseApp.verificationToken.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final RoleRepository roleRepository;
   private final VerificationTokenRepository verificationTokenRepository;
   private final MessageSource messageSource;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, VerificationTokenRepository verificationTokenRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.messageSource = messageSource;
    }

    @Transactional(dontRollbackOn = UsernameNotFoundException.class)
    public User registerNewUserAccount(UserRegistrationDto userDto){
        if(emailExists(userDto.getEmail())){
            throw new UsernameNotFoundException("An account with this email address already exists.");
        }else if(usernameExists(userDto.getUsername())){
            throw new UsernameNotFoundException("An account with this username already exists.");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        Role role = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        return userRepository.save(user);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }


    public void saveRegisteredUser(User user) {
       userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

}
