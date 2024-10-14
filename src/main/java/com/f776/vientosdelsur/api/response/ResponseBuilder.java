package com.f776.vientosdelsur.api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
public class ResponseBuilder {

    private static final Function<Exception, String> errorMessage = (e) -> {
        if (e instanceof DisabledException) {
            return "La cuenta está deshabilitada. Por favor verifique la cuenta en su correo.";
        }
        if (e instanceof LockedException) {
            return "La cuenta está bloqueada";
        }
        if (e instanceof BadCredentialsException) {
            return "Credenciales incorrectas";
        }
        if (e instanceof CredentialsExpiredException) {
            return "Las credenciales han expirado";
        }
        if (e instanceof NoContentException) {
            return e.getMessage();
        }

        return "Ocurrió un error. Por favor intente de nuevo.";
    };

    private static final BiConsumer<HttpServletResponse, ApiResponse> writeResponse = (httpServletResponse, apiResponse) -> {
        try {
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, apiResponse);
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    };

    public static void handleErrorResponse(HttpServletResponse response, Exception e) {
        ApiResponse apiResponse = getErrorResponse(response, e);
        writeResponse.accept(response, apiResponse);
    }

    private static ApiResponse getErrorResponse(HttpServletResponse response, Exception e) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return new ApiResponse(errorMessage.apply(e), Map.of());
    }
}
