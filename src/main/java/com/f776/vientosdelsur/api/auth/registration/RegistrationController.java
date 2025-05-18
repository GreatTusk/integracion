package com.f776.vientosdelsur.api.auth.registration;

import com.f776.vientosdelsur.api.response.ApiResponse;
import com.f776.vientosdelsur.api.response.ResourceAlreadyExistsException;
import com.f776.vientosdelsur.api.user.Department;
import com.f776.vientosdelsur.api.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object register(HttpServletRequest request, HttpServletResponse response) {
        try {
            RegistrationRequest registrationRequest;
            boolean isJsonRequest = request.getContentType() != null &&
                    request.getContentType().contains("application/json");

            if (isJsonRequest) {
                // Handle JSON request
                registrationRequest = objectMapper.readValue(request.getInputStream(), RegistrationRequest.class);
                log.debug("Processing JSON registration request");
            } else {
                // Handle form submission
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                String roleStr = request.getParameter("role");
                String departmentStr = request.getParameter("department");

                Role role = roleStr != null ? Role.valueOf(roleStr) : null;
                Department department = departmentStr != null ? Department.valueOf(departmentStr) : null;

                registrationRequest = RegistrationRequest.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(password)
                        .confirmPassword(confirmPassword)
                        .role(role)
                        .department(department)
                        .build();

                log.debug("Processing form registration request for {}", email);
            }

            URI employeeURI = registrationService.register(registrationRequest);

            // Handle response based on request type
            if (isJsonRequest) {
                return ResponseEntity.created(employeeURI).build();
            } else {
                // For form submissions, redirect to success page
                response.sendRedirect("/auth/success");
                return null;
            }
        } catch (ResourceAlreadyExistsException e) {
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
            } else {
                try {
                    response.sendRedirect("/auth/register?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    log.error("Failed to redirect", ex);
                }
                return null;
            }
        } catch (Exception e) {
            log.error("Registration failed", e);
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
            } else {
                try {
                    response.sendRedirect("/auth/register?error=" + URLEncoder.encode("Registration failed", StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    log.error("Failed to redirect", ex);
                }
                return null;
            }
        }
    }
}