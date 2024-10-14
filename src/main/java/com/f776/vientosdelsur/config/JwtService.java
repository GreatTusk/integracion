package com.f776.vientosdelsur.config;

import com.f776.vientosdelsur.api.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService implements IJwtService {

    @Value("${spring.keys.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.keys.jwt.expiration}")
    private Long expiration;

    private final UserDetailsService userDetailsService;

    private final Supplier<SecretKey> signInKey = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    private final Function<String, Claims> extractAllClaims = token -> Jwts
            .parser()
            .verifyWith(signInKey.get())
            .build()
            .parseSignedClaims(token)
            .getPayload();

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken = (request, cookieName) -> {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), cookieName))
                .map(Cookie::getValue)
                .findAny();
    };

    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie = (request, cookieName) -> {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), cookieName))
                .findAny();
    };

    private final Supplier<JwtBuilder> builder = () ->
            Jwts.builder()
                    .header().add(Map.of(Header.TYPE, Header.JWT_TYPE))
                    .and()
                    .audience().add("Vientos del Sur")
                    .and()
                    .id(UUID.randomUUID().toString())
                    .issuedAt(Date.from(Instant.now()))
                    .notBefore(new Date())
                    .signWith(signInKey.get());

    private final BiFunction<UserDetails, TokenType, String> buildToken = (user, tokenType) ->
            Objects.equals(tokenType, TokenType.ACCESS) ?
                    builder.get()
                            .claim("role", user.getAuthorities())
                            .subject(user.getUsername())
                            .expiration(Date.from(Instant.now().plusSeconds(expiration)))
                            .compact() :
                    builder.get()
                            .subject(user.getUsername())
                            .expiration(Date.from(Instant.now().plusSeconds(expiration)))
                            .compact();

    private final TriConsumer<HttpServletResponse, UserDetails, TokenType> addCookie = (httpServletResponse, user, tokenType) -> {

        switch (tokenType) {
            case ACCESS -> {
                String accessToken = generateToken(user, Token::getAccess);
                Cookie cookie = new Cookie(tokenType.getValue(), accessToken);
                cookie.setHttpOnly(true);
//                cookie.setSecure(true);
                cookie.setMaxAge(2 * 60);
                cookie.setPath("/");
                cookie.setAttribute("SameSite", org.springframework.boot.web.server.Cookie.SameSite.NONE.name());
                httpServletResponse.addCookie(cookie);
            }
            case REFRESH -> {
                String refresh = generateToken(user, Token::getRefresh);
                Cookie cookie = new Cookie(tokenType.getValue(), refresh);
                cookie.setHttpOnly(true);
//                cookie.setSecure(true);
                cookie.setMaxAge(2 * 60 * 60);
                cookie.setPath("/");
                cookie.setAttribute("SameSite", org.springframework.boot.web.server.Cookie.SameSite.NONE.name());
                httpServletResponse.addCookie(cookie);
            }
        }
    };

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return extractAllClaims.andThen(claimsResolver).apply(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    @Override
    public String generateToken(UserDetails userDetails, Function<Token, String> tokenFunc) {
        Token token = Token.builder()
                .access(buildToken.apply(userDetails, TokenType.ACCESS))
                .refresh(buildToken.apply(userDetails, TokenType.REFRESH))
                .build();

        return tokenFunc.apply(token);
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, String cookieName) {
        return extractToken.apply(request, cookieName);
    }

    @Override
    public void addCookie(HttpServletResponse response, UserDetails userDetails, TokenType tokenType) {
        addCookie.accept(response, userDetails, tokenType);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunc) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractUserEmail(token));
        return tokenFunc.apply(
                TokenData.builder()
                        .valid(Objects.equals(userDetails.getUsername(), extractUserEmail(token)))
                        .claims(extractAllClaims.apply(token))
                        .user(userDetails)
                        .build()
        );
    }

    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        extractCookie.apply(request, cookieName)
                .ifPresent(cookie -> {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
    }
}
