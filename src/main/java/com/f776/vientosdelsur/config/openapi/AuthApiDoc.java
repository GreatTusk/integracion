package com.f776.vientosdelsur.config.openapi;

import com.f776.vientosdelsur.api.auth.login.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Authentication API")
public interface AuthApiDoc {

    @Operation(summary = "Login to the application", description = "Authenticates user and returns JWT tokens as cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "429", description = "Too many login attempts")
    })
    @PostMapping("/api/v1/auth/login")
    void login(@RequestBody LoginRequest loginRequest);
}

