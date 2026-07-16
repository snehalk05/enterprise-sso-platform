package com.snehal.sso.auth.exception;

import com.snehal.sso.exceptions.ApiError;
import com.snehal.sso.exceptions.BusinessException;
import com.snehal.sso.exceptions.NotFoundException;
import com.snehal.sso.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException e, HttpServletRequest r) {
        Map<String, String> f = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> f.put(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ApiError(Instant.now(), 400, "Validation failed", "Invalid request", r.getRequestURI(), f));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiError> missing(NotFoundException e, HttpServletRequest r) {
        return error(404, "Not found", e.getMessage(), r);
    }

    @ExceptionHandler({BusinessException.class, UnauthorizedException.class})
    ResponseEntity<ApiError> business(RuntimeException e, HttpServletRequest r) {
        return error(400, "Request rejected", e.getMessage(), r);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> other(Exception e, HttpServletRequest r) {
        return error(500, "Internal server error", "Unexpected error", r);
    }

    private ResponseEntity<ApiError> error(int s, String er, String m, HttpServletRequest r) {
        return ResponseEntity.status(s).body(new ApiError(Instant.now(), s, er, m, r.getRequestURI(), Map.of()));
    }
}
