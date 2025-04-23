package com.f776.vientosdelsur.api.auth.login;

import com.f776.vientosdelsur.api.response.ApiResponse;
import com.f776.vientosdelsur.api.response.ResponseBuilder;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.jwt.IJwtService;
import com.f776.vientosdelsur.jwt.token.TokenType;
import com.f776.vientosdelsur.utils.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public static final String LOGIN_PATH = "/api/v1/auth/login";
    private final IJwtService jwtService;
    private final ILoginService loginService;

    public LoginFilter(AuthenticationManager authenticationManager, ILoginService loginService, IJwtService jwtService) {
        // Listen to log in attempts on path
        super(new AntPathRequestMatcher(LOGIN_PATH, HttpMethod.POST.name()), authenticationManager);
        this.jwtService = jwtService;
        this.loginService = loginService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            LoginRequest loginRequest = new ObjectMapper()
                    .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                    .readValue(request.getInputStream(), LoginRequest.class);
            loginService.updateLoginAttempt(loginRequest.getEmail(), LoginType.LOGIN_ATTEMPT);
            log.info("Login attempt for email: {}", loginRequest.getEmail());

            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseBuilder.handleErrorResponse(response, e);
            return null;
        }
    }


    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {

        try {
            var user = (User) authentication.getPrincipal();
            log.info("User authenticated: {}", user.getUsername());

            loginService.updateLoginAttempt(user.getUsername(), LoginType.LOGIN_SUCCESS);
            log.info("Login attempt updated for user: {}", user.getUsername());

            record UserResponse(
                    String message,
                    String department,
                    String role
            ) {
            }

            ApiResponse apiResponse = new ApiResponse("Successful login", new UserResponse(
                    user.getUsername(),
                    user.getDepartment().toString(),
                    user.getRole().toString()
            ));

            jwtService.addCookie(response, user, TokenType.ACCESS);
            jwtService.addCookie(response, user, TokenType.REFRESH);
            log.info("JWT cookies added for user: {}", user.getUsername());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            ServletOutputStream outputStream = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outputStream, apiResponse);
            outputStream.flush();
            log.info("Response sent for user: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error during successful authentication", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ApiResponse apiResponse = new ApiResponse("Unauthorized", "Full authentication is required to access this resource");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), apiResponse);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        log.error("Unsuccessful authentication", failed);
    }
}
