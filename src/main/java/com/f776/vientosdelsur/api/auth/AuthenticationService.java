package com.f776.vientosdelsur.api.auth;

import com.f776.vientosdelsur.api.email.IEmailService;
import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import com.f776.vientosdelsur.api.response.NoContentException;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import com.f776.vientosdelsur.api.user.verification.AccountVerification;
import com.f776.vientosdelsur.api.user.verification.AccountVerificationRepository;
import com.f776.vientosdelsur.cache.CacheStore;
import com.f776.vientosdelsur.config.JwtService;
import com.f776.vientosdelsur.config.login.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    public static final int MAX_LOGIN_ATTEMPTS = 5;
    private final UserRepository userRepository;
    private final AccountVerificationRepository accountVerificationRepository;
    private final IEmailService emailService;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheStore<String, Integer> userCache;

    @Override
    public void register(RegisterRequest request) {

        User user = User
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(false)
                .accountNonLocked(true)
                .loginAttempts(0)
                .build();

        Employee employee = Employee
                .builder()
                .occupation(request.getOccupation())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dayOff(request.getDayOff())
                .phoneNumber(request.getPhoneNumber())
                .entryDate(request.getEntryDate())
                .user(user)
                .build();

        employeeRepository.save(employee);

        AccountVerification accountVerification = new AccountVerification(user);
        accountVerificationRepository.save(accountVerification);

        emailService.sendVerificationEmail(request.getFirstName(), request.getEmail(), accountVerification.getVerificationToken());
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        User userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoContentException("No se ha podido encontrar la cuenta. Por favor inicie sesión con una cuenta válida."));

        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userDetails.getEmail()) == null) {
                    userDetails.setLoginAttempts(0);
                    userDetails.setAccountNonLocked(true);
                }

                userDetails.setLoginAttempts(userDetails.getLoginAttempts() + 1);
                userCache.put(userDetails.getEmail(), userDetails.getLoginAttempts());

                if (userCache.get(userDetails.getEmail()) > MAX_LOGIN_ATTEMPTS) {
                    userDetails.setAccountNonLocked(false);
                }

            }
            case LOGIN_SUCCESS -> {
                userDetails.setAccountNonLocked(true);
                userDetails.setLoginAttempts(0);
                userDetails.setLastLogin(LocalDateTime.now());
                userCache.evict(userDetails.getEmail());
            }
        }

        userRepository.save(userDetails);
    }
}
