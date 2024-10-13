package com.f776.vientosdelsur.api.user.verification;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@AllArgsConstructor
public class AccountVerificationService implements IAccountVerificationService {

    private final AccountVerificationRepository accountVerificationRepository;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public String verifyEmail(String token) {
        AccountVerification accountVerification = accountVerificationRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token not valid"));

        User user = accountVerification.getUser();
        user.setIsEnabled(true);
        userRepository.save(user);

        accountVerificationRepository.delete(accountVerification);

        Employee employee = user.getEmployee();

        Context context = new Context();
        context.setVariables(Map.of("nombre", employee.getFirstName() + " " + employee.getLastName(),
                "email", user.getEmail()));

        return templateEngine.process("email-verificado", context);
    }
}
