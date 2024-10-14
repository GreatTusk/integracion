package com.f776.vientosdelsur.email;

public interface IEmailService {
    void sendVerificationEmail(String name, String to, String token);
}
