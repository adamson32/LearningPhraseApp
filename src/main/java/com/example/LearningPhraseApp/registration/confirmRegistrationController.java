package com.example.LearningPhraseApp.registration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/confirmRegistration")
@Controller
public class confirmRegistrationController {

    @GetMapping
    String show(@RequestParam("tokenMessage") String message, Model model){
        model.addAttribute("tokenMessage", message);
        return "confirmRegistration";

    }
}
