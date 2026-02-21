package net.daifo.springbootgateway.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handles @Valid failures
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidation(WebExchangeBindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity.badRequest().body("{ \"error\": \"" + message + "\" }");
    }
    // Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{ \"error\": \"" + ex.getMessage() + "\" }");
    }
}