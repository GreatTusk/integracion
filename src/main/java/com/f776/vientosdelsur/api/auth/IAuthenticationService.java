package com.f776.vientosdelsur.api.auth;

import com.f776.vientosdelsur.config.login.LoginType;

public interface IAuthenticationService {
    void register(RegisterRequest request);
    void updateLoginAttempt(String email, LoginType loginType);
}
