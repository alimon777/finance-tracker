package com.finance.transaction.exceptions;

import com.finance.transaction.dto.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        CustomResponse<?> response = new CustomResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccountCreationException.class)
    public ResponseEntity<CustomResponse<?>> handleAccountCreationException(AccountCreationException ex) {
        CustomResponse<?> response = new CustomResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<?>> handleGeneralException(Exception ex) {
        CustomResponse<?> response = new CustomResponse<>("An unexpected error occurred: " + ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(DuplicateAccountNumberException.class)
    public ResponseEntity<CustomResponse<?>> handleDuplicateAccountNumberException(DuplicateAccountNumberException ex) {
        CustomResponse<?> response = new CustomResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // Use 409 Conflict status code
    }
    
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<CustomResponse<?>> handleInsufficientFundsException(InsufficientFundsException ex) {
        CustomResponse<?> response = new CustomResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
    }
}
