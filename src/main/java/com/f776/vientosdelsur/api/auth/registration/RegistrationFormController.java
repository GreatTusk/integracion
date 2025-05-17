package com.f776.vientosdelsur.api.auth.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationFormController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute RegistrationRequest request,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                redirectAttributes.addAttribute("error", "Contraseñas no coinciden");
                return "redirect:/auth/register";
            }

            registrationService.register(request);
            return "redirect:/auth/success";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
