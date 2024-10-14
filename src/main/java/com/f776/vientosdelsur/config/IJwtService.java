package com.f776.vientosdelsur.config;

import com.f776.vientosdelsur.api.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.function.Function;

public interface IJwtService {
    String generateToken(UserDetails userDetails, Function<Token, String> tokenFunc);
    Optional<String> extractToken(HttpServletRequest request, String tokenType);
    void addCookie(HttpServletResponse response, UserDetails userDetails, TokenType tokenType);
    <T> T getTokenData(String token, Function<TokenData, T> tokenFunc);
    void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);
}
