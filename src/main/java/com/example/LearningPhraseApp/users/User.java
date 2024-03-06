package com.example.LearningPhraseApp.users;


import com.example.LearningPhraseApp.group_phrases.GroupPhrases;
import com.example.LearningPhraseApp.roles.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private String email;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")

    private List<GroupPhrases> groupPhrases;

    public User() {
        super();
        this.enabled = false;
    }


}

