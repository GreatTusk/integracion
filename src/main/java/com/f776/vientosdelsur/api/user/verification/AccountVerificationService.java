package com.f776.vientosdelsur.api.user.verification;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.response.NoContentException;
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
    public String verifyEmail(String token) throws NoContentException {
        Context context = new Context();

        AccountVerification accountVerification = accountVerificationRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NoContentException(templateEngine.process("404", context)));

        User userDetails = accountVerification.getUser();
        userDetails.setEnabled(true);
        userRepository.save(userDetails);

        accountVerificationRepository.delete(accountVerification);

        Employee employee = userDetails.getEmployee();

        context.setVariables(Map.of("nombre", employee.getFirstName() + " " + employee.getLastName(),
                "email", userDetails.getEmail()));

        return templateEngine.process("email-verificado", context);
    }
}
