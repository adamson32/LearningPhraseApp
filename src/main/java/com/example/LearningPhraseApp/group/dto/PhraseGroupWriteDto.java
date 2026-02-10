package com.example.LearningPhraseApp.group.dto;

import com.example.LearningPhraseApp.users.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhraseGroupWriteDto {
    @NotBlank(message = "name must be not null")
    @Length(max = 100)
    private String name;
    @Length(max = 1000)
    private String description;
    private String imageUrl;
    private User user;


}
