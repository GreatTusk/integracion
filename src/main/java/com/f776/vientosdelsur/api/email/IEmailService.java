package com.f776.vientosdelsur.api.email;

public interface IEmailService {
    void sendVerificationEmail(String name, String to, String token);
}
