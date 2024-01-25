package tacos.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.data.UserRepository;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "registrationForm")
    @SuppressWarnings("unused")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    @GetMapping
    @SuppressWarnings("unused")
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    @SuppressWarnings("unused")
    public String processRegistration(final @Valid RegistrationForm registrationForm, final Errors errors) {

        if (errors.hasErrors()) {
            return "registration";
        }

        userRepository.save(registrationForm.toUser(passwordEncoder));
        log.info("Processing registration form...");

        return "redirect:/login";
    }

}
