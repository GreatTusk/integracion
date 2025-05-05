package com.f776.vientosdelsur.config.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request payload")
public class LoginRequestDto {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User password", example = "password", required = true)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
