package com.example.LearningPhraseApp.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noAccess")
public class NoAccessController {

    @GetMapping
    String showNoAccessPage() {
        return "noAccess";
    }
}
