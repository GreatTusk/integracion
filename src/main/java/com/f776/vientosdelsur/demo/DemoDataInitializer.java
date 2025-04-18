package com.f776.vientosdelsur.demo;

import com.f776.vientosdelsur.api.user.Role;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Transactional
@Order(4)
public class DemoDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    /*
     * This class initializes tables that in production would contain dynamic data.
     * Other "...Initializer" classes take care of populating tables that will always contain the
     * same data.
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        addSampleUsersIfNotExists();
    }

    private void addSampleUsersIfNotExists() {

        Role[] roles = Role.values();
        Random seed = new Random();
        for (Role role : roles) {
            for (int j = 0; j < 5; j++) {
                String defaultEmail = "user" + seed.nextInt(100, 999) + "@email.com";

                if (userRepository.existsByEmail(defaultEmail)) {
                    continue;
                }

                User userDetails = User
                        .builder()
                        .email(defaultEmail)
                        .password(passwordEncoder.encode("Contrasena.123"))
                        .role(role)
                        .enabled(true)
                        .accountNonLocked(true)
                        .loginAttempts(seed.nextInt(1, 16))
                        .lastLogin(LocalDateTime.now())
                        .build();

                userRepository.save(userDetails);
            }
        }
    }

}
