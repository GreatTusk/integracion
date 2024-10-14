package com.f776.vientosdelsur.config.auth;

import com.f776.vientosdelsur.jwt.IJwtService;
import com.f776.vientosdelsur.jwt.token.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthRequestFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        jwtService.extractToken(request, TokenType.ACCESS)
                .ifPresent(token -> {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        String userEmail = jwtService.extractUserEmail(token);
                        // Will throw UsernameNotFoundException if not found
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                        if (!jwtService.isTokenExpired(token)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                });

        filterChain.doFilter(request, response);
    }
}
