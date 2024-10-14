package com.f776.vientosdelsur.api.auth.verification;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountVerificationRepository extends JpaRepository<AccountVerification, Long> {
    Optional<AccountVerification> findByVerificationToken(@NotNull String verificationToken);
}
