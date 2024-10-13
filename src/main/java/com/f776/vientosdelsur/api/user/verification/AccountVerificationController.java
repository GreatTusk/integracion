package com.f776.vientosdelsur.api.user.verification;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/verify")
public class AccountVerificationController {

    private final IAccountVerificationService accountVerificationService;

    @GetMapping
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        return ResponseEntity.ok(accountVerificationService.verifyEmail(token));
    }

}
