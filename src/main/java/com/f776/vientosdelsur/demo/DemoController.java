package com.f776.vientosdelsur.demo;

import com.f776.vientosdelsur.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class DemoController {
    @PreAuthorize("hasRole('ROLE_VENTAS_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> sayHelloToAdmin() {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return ResponseEntity.ok("Hello from secured endpoint, " + username + ", you are an admin!");
    }
}
