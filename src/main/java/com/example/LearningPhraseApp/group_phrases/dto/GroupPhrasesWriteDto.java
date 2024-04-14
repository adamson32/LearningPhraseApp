package com.example.LearningPhraseApp.group_phrases.dto;

import com.example.LearningPhraseApp.users.User;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupPhrasesWriteDto {
    @NotBlank(message = "name must be not null")
    @Length(max = 100)
    private String name;
    @Length(max = 1000)
    private String description;
    private String imageUrl;
    private User user;


}
