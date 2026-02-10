package com.example.LearningPhraseApp.group;

import com.example.LearningPhraseApp.pharses.Phrases;
import com.example.LearningPhraseApp.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<Phrases> phrases;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
