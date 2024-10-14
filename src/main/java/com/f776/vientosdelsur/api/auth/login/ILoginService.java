package com.f776.vientosdelsur.api.auth.login;

public interface ILoginService {
    void updateLoginAttempt(String email, LoginType loginType);
}
