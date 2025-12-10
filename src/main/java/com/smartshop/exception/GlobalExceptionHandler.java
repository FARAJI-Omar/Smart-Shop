package com.smartshop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation error");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handle type mismatch errors
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    // ===== Resource Not Found Exceptions  =====

    @ExceptionHandler({
            ClientNotFoundException.class,
            ProductNotFoundException.class,
            OrderNotFoundException.class,
            PaymentNotFoundException.class,
            PromoCodeNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            RuntimeException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    // ===== Unauthorized Access  =====

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessException(
            UnauthorizedAccessException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    }

    // ===== Authentication Errors  =====

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    // ===== Business Rule Violations  =====

    @ExceptionHandler({
            OrderNotPendingException.class,
            OrderNotFullyPaidException.class,
            OrderHasPendingPaymentsException.class,
            InvalidPaymentAmountException.class,
            PaymentExceedsRemainingAmountException.class,
            CannotModifyCashPaymentException.class,
            PaymentAlreadyProcessedException.class,
            InvalidPaymentStatusException.class,
            PromoCodeAlreadyUsedException.class,
            CannotDeleteAdminException.class,
            UserDeletionException.class,
            CannotPayMoreThan2KInCashException.class
    })
    public ResponseEntity<Map<String, Object>> handleBusinessRuleViolationException(
            RuntimeException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    // ===== java Exception Handlers  =====

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(
            SecurityException ex,
            HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI());
    }

    /**
     * error response
     * Format: timestamp, status, error, message, path
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status,
            String message,
            String path) {

        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", path);

        return ResponseEntity.status(status).body(errorResponse);
    }
}

