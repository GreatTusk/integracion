package com.f776.vientosdelsur.api.user.verification;

import com.f776.vientosdelsur.api.response.NoContentException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            return ResponseEntity.ok(accountVerificationService.verifyEmail(token));
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
