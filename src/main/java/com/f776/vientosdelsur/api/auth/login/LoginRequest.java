package com.f776.vientosdelsur.api.auth.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "Ingrese su email.")
    @Email(message = "Ingrese un correo válido.")
    private String email;
    @NotEmpty(message = "Ingrese su contraseña.")
    private String password;
}
