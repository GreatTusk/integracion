package com.f776.vientosdelsur.api.user.verification;

public interface IAccountVerificationService {
    String verifyEmail(String token);
}
