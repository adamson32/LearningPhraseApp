package com.example.LearningPhraseApp.users;


import com.example.LearningPhraseApp.group.PhraseGroup;
import com.example.LearningPhraseApp.roles.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<PhraseGroup> phraseGroupList;

    public User() {
        super();
        this.enabled = false;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}

