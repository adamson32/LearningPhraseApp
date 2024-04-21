package com.example.LearningPhraseApp.registration;

import com.example.LearningPhraseApp.users.*;
import com.example.LearningPhraseApp.verificationToken.VerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final VerificationTokenService verificationTokenService;

    public RegistrationController(ApplicationEventPublisher eventPublisher, UserService userService,
                                  VerificationTokenService verificationTokenService) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    @GetMapping
    String show(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

@Transactional
@PostMapping()
public ModelAndView registerUserAccount(
        @ModelAttribute("user") @Valid UserRegistrationDto userDto,
        HttpServletRequest request) {

    try {
        User registered = userService.registerNewUserAccount(userDto);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                request.getLocale(), appUrl));
    } catch (UsernameNotFoundException uaeEx) {
        ModelAndView mav = new ModelAndView("registration");
        mav.addObject("registrationMessage", uaeEx.getMessage());
        return mav;
    }catch(RuntimeException ex){
        logger.error(ex.getMessage());
        return new ModelAndView("redirect:/somethingWentWrong");
    }
    String modelObject = "\"Successfully registered, if you haven't received a confirmation link, click the button below.";
    return new ModelAndView("redirect:/confirmRegistration","tokenMessage",
            modelObject);
}

    @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration
            (@RequestParam("token") String token
             ) {

        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        if (verificationToken == null) {
            return new ModelAndView("redirect:/badUser");

        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ModelAndView("/confirmRegistration","tokenMessage","The token has expired," +
                    " click the button below to resend the confirmation link.");
        }
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
       return new ModelAndView("redirect:/login");
    }

}
