package com.f776.vientosdelsur.api.auth;

import com.f776.vientosdelsur.api.user.*;
import com.f776.vientosdelsur.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Employee employee = Employee
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dayOff(request.getDayOff())
                .phoneNumber(request.getPhoneNumber())
                .entryDate(request.getEntryDate())
                .build();

        employeeRepository.save(employee);

        User user = User
                .builder()
                .employee(employee)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(Map.of("role", request.getRole()), user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(Map.of("role", user.getRole()), user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
