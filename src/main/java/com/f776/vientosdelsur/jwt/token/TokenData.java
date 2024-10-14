package com.f776.vientosdelsur.jwt.token;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Data
public class TokenData {
    private UserDetails user;
    private Claims claims;
    private boolean valid;
//    private List<GrantedAuthority> authorities;
}
