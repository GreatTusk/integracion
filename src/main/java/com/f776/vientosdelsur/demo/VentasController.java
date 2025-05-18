package com.f776.vientosdelsur.demo;

import com.f776.vientosdelsur.api.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VentasController {
    @PreAuthorize("hasRole('ROLE_VENTAS_ADMIN')")
    @GetMapping("/ventas")
    public String ventasAdminPage(Model model) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("username", username);
        return "admin-ventas";
    }
}
