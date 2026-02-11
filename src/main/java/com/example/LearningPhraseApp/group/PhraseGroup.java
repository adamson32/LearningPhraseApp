package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.pharses.Phrases;
import com.example.LearningPhraseApp.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Getter
@Entity
@Table(name = "phrase_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhraseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private String imageUrl;
    private LocalDateTime nextDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
    @Getter(AccessLevel.NONE)
    private List<Phrases> phrases;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public List<Phrases> getPhrases() {
        return Collections.unmodifiableList(phrases);
    }
}
