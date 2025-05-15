package com.f776.vientosdelsur.jwt;

import com.f776.vientosdelsur.jwt.token.Token;
import com.f776.vientosdelsur.jwt.token.TokenData;
import com.f776.vientosdelsur.jwt.token.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService implements IJwtService {

    private static final String BEARER_PREFIX = "Bearer ";

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

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractBearerToken = (request, tokenType) -> {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring(BEARER_PREFIX.length()));
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
                            .expiration(Date.from(Instant.now().plusSeconds(expiration * 12))) // Refresh token lives longer
                            .compact();

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return extractAllClaims.andThen(claimsResolver).apply(token);
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
    public Token generateTokenPair(UserDetails userDetails) {
        return Token.builder()
                .access(buildToken.apply(userDetails, TokenType.ACCESS))
                .refresh(buildToken.apply(userDetails, TokenType.REFRESH))
                .build();
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, TokenType tokenType) {
        return extractBearerToken.apply(request, tokenType.getValue());
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
}
