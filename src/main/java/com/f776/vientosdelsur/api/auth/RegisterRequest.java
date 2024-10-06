package com.f776.vientosdelsur.api.auth;

import com.f776.vientosdelsur.api.employee.occupation.Occupation;
import com.f776.vientosdelsur.api.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private Role role;
    private DayOfWeek dayOff;
    @NotNull
    private LocalDate entryDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Occupation occupation;
}