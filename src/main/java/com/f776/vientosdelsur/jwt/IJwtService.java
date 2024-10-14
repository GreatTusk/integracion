package com.f776.vientosdelsur.jwt;

import com.f776.vientosdelsur.jwt.token.Token;
import com.f776.vientosdelsur.jwt.token.TokenData;
import com.f776.vientosdelsur.jwt.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.function.Function;

public interface IJwtService {
    String generateToken(UserDetails userDetails, Function<Token, String> tokenFunc);
    Optional<String> extractToken(HttpServletRequest request, TokenType tokenType);
    void addCookie(HttpServletResponse response, UserDetails userDetails, TokenType tokenType);
    String extractUserEmail(String token);
    boolean isTokenExpired(String token);
    <T> T getTokenData(String token, Function<TokenData, T> tokenFunc);
    void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);
}
