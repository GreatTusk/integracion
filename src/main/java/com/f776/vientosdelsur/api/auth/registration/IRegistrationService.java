package com.f776.vientosdelsur.api.auth.registration;

import com.f776.vientosdelsur.api.response.ResourceAlreadyExistsException;
import com.f776.vientosdelsur.api.auth.login.LoginType;

import java.net.URI;

public interface IRegistrationService {
    URI register(RegistrationRequest request) throws ResourceAlreadyExistsException;
    void updateLoginAttempt(String email, LoginType loginType);
}
