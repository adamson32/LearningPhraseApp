package com.example.LearningPhraseApp.users;

import com.example.LearningPhraseApp.roles.Role;
import com.example.LearningPhraseApp.roles.RoleEnum;
import com.example.LearningPhraseApp.roles.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                              RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(dontRollbackOn = UsernameNotFoundException.class)
    public User registerNewUserAccount(UserRegistrationDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new UsernameNotFoundException("An account with this email address already exists.");
        } else if (usernameExists(userDto.getUsername())) {
            throw new UsernameNotFoundException("An account with this username already exists.");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        Role role = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.addRole(role);
        return userRepository.save(user);
    }

    @Override
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
