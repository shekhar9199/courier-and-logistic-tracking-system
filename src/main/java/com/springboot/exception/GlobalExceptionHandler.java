package com.springboot.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.springboot.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // FIX: Use one response shape and map missing resources consistently.
    @ExceptionHandler({IdNotFoundException.class, NoRecordAvailableException.class})
    public ResponseEntity<ResponseStructure<String>> handleNotFound(RuntimeException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), "No record found");
    }

    @ExceptionHandler({DetailsAllreadyExistException.class, PaymentAlreadyUsedException.class})
    public ResponseEntity<ResponseStructure<String>> handleConflict(RuntimeException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), "Operation conflicts with existing data");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseStructure<String>> handleIllegalState(IllegalStateException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), "Operation not allowed");
    }

    // FIX: Service-level invalid input now returns 400 rather than an unhandled 500.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseStructure<String>> handleIllegalArgument(IllegalArgumentException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), "Invalid request");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseStructure<String>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = "Invalid value: " + exception.getValue();
        Class<?> requiredType = exception.getRequiredType();
        if (requiredType != null && requiredType.isEnum()) {
            String allowed = Arrays.stream(requiredType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            message += ". Allowed values: " + allowed;
        }
        return response(HttpStatus.BAD_REQUEST, message, "Invalid request parameter");
    }

    // FIX: Bean validation errors have a concise, consistent API response.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<String>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, message, "Validation failed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseStructure<String>> handleConstraintViolation(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, message, "Validation failed");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseStructure<String>> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return response(HttpStatus.BAD_REQUEST, "Request body is malformed or contains an invalid value", "Invalid request body");
    }

    // FIX: Database constraint races and FK violations return a safe conflict response.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseStructure<String>> handleDataIntegrity(DataIntegrityViolationException exception) {
        return response(HttpStatus.CONFLICT, "Request conflicts with existing data or database constraints", "Data integrity violation");
    }

    private ResponseEntity<ResponseStructure<String>> response(HttpStatus status, String message, String data) {
        ResponseStructure<String> body = new ResponseStructure<>();
        body.setStatusCode(status.value());
        body.setMessage(message);
        body.setData(data);
        return ResponseEntity.status(status).body(body);
    }
}
