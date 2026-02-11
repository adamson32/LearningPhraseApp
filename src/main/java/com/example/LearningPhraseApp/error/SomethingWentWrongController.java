package com.example.LearningPhraseApp.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/somethingWentWrong")
public class SomethingWentWrongController {

    @GetMapping
    String showTemplate() {
        return "somethingWentWrong";
    }
}
