package com.f776.vientosdelsur.api.auth.registration;

import com.f776.vientosdelsur.api.auth.verification.AccountVerification;
import com.f776.vientosdelsur.api.auth.verification.AccountVerificationRepository;
import com.f776.vientosdelsur.api.response.ResourceAlreadyExistsException;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import com.f776.vientosdelsur.cache.CacheStore;
import com.f776.vientosdelsur.email.IEmailService;
import com.f776.vientosdelsur.utils.Constants;
import com.f776.vientosdelsur.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class RegistrationService implements IRegistrationService {

    public static final int MAX_LOGIN_ATTEMPTS = 5;
    private final CacheStore<String, Integer> userCache;
    private final UserRepository userRepository;
    private final AccountVerificationRepository accountVerificationRepository;
    private final IEmailService emailService;
//    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public URI register(RegistrationRequest request) throws ResourceAlreadyExistsException {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con este correo. Ingrese un correo válido.");
        }

        User user = User
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(false)
                .accountNonLocked(true)
                .loginAttempts(0)
                .build();

        AccountVerification accountVerification = new AccountVerification(user);
        accountVerificationRepository.save(accountVerification);

        emailService.sendVerificationEmail(Utils.initCap(request.getFirstName() + " " + request.getLastName()),
                request.getEmail(),
                accountVerification.getVerificationToken());

        return URI.create("");
//        return Constants.buildEmployeeURI.apply(employee.getId());
    }
}
