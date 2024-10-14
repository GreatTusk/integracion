package com.f776.vientosdelsur.api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Slf4j
public class ResponseBuilder {

    private static final BiFunction<Exception, HttpStatus, String> errorMessage = (e, httpStatus) -> {
        if (httpStatus == HttpStatus.FORBIDDEN) {
            return "No tiene los permisos suficientes";
        }
        if (httpStatus == HttpStatus.UNAUTHORIZED) {
            return "No ha iniciado sesión";
        }
        if (e instanceof DisabledException) {
            return "La cuenta está deshabilitada";
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
            return "No se encontró contenido";
        }
        if (httpStatus.is5xxServerError()) {
            return "Error interno del servidor";
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

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (e instanceof AccessDeniedException) {
            ApiResponse apiResponse = getErrorResponse(request, response, e, HttpStatus.FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        }
    }

    private static ApiResponse getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e, HttpStatus httpStatus) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return new ApiResponse(errorMessage.apply(e, httpStatus), Map.of());
    }
}
