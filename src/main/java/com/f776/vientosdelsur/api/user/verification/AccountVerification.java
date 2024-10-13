package com.f776.vientosdelsur.api.user.verification;

import com.f776.vientosdelsur.api.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class AccountVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @NotNull
    private User user;

    @NotNull
    private String verificationToken;

    public AccountVerification(User user) {
        this.user = user;
        this.verificationToken = UUID.randomUUID().toString();
    }
}
