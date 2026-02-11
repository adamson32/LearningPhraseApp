package com.example.LearningPhraseApp.pharses;

import com.example.LearningPhraseApp.group.PhraseGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "phrases")
@JsonIgnoreProperties({"group"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class Phrases {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String phrase;
    private String description;
    private String meaning;
    private int progress;
    @CreationTimestamp
    private LocalDateTime nextDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phrase_group_id")
    @Setter(AccessLevel.NONE)
    private PhraseGroup group;

    public Phrases(String phrase, String description,
                   String polishMeaning, LocalDateTime nextDate, PhraseGroup group) {
        this.phrase = phrase;
        this.description = description;
        this.meaning = polishMeaning;
        this.nextDate = nextDate;
        if (group != null) {
            this.group = group;
        }
    }

    public void updateFrom(final Phrases source) {
        phrase = source.phrase;
        description = source.description;
        meaning = source.meaning;
    }

    public void assignToGroup(PhraseGroup group) {
        this.group = group;
    }

    @PrePersist
    void prePersists() {
        progress = 1;
    }


}
