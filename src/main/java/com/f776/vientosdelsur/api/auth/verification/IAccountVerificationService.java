package com.f776.vientosdelsur.api.auth.verification;

import com.f776.vientosdelsur.api.response.NoContentException;

public interface IAccountVerificationService {
    String verifyEmail(String token) throws NoContentException;
}
