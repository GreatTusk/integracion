package com.f776.vientosdelsur.demo;

import com.f776.vientosdelsur.api.user.Department;
import com.f776.vientosdelsur.api.user.Role;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import com.f776.vientosdelsur.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

        var roles = Arrays.stream(Role.values()).toList();
        var departments = Arrays.stream(Department.values()).toList();
        Random seed = new Random();


        var users = new ArrayList<User>();

        users.add(
                User
                        .builder()
                        .department(Utils.pickRandom(departments))
                        .email("mymail@gmail.com")
                        .password(passwordEncoder.encode("Contrasena.123"))
                        .role(Utils.pickRandom(roles))
                        .enabled(true)
                        .accountNonLocked(true)
                        .loginAttempts(seed.nextInt(1, 16))
                        .lastLogin(LocalDateTime.now())
                        .build()
        );

        for (int j = 0; j < 5; j++) {
            String defaultEmail = "user" + seed.nextInt(100, 999) + "@email.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User userDetails = User
                    .builder()
                    .department(Utils.pickRandom(departments))
                    .email(defaultEmail)
                    .password(passwordEncoder.encode("Contrasena.123"))
                    .role(Utils.pickRandom(roles))
                    .enabled(true)
                    .accountNonLocked(true)
                    .loginAttempts(seed.nextInt(1, 16))
                    .lastLogin(LocalDateTime.now())
                    .build();

            users.add(userDetails);
        }

        userRepository.saveAll(users);
    }
}
