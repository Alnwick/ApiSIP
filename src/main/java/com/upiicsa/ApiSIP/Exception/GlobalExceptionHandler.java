package com.upiicsa.ApiSIP.Exception;

import com.upiicsa.ApiSIP.Dto.ErrorResponseDto;
import com.upiicsa.ApiSIP.Model.Enum.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                code.getHttpStatus().value(),
                code.getHttpStatus().getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, code.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensajeErrores = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce("", (a, b) -> a + b + ". ");

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validación en los datos enviados: " + mensajeErrores.trim()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDatabaseExceptions(DataIntegrityViolationException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Error de integridad de datos. Es posible que el registro ya exista o viole una restricción."
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(RuntimeException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleLegacyValidationException(ValidationException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(Exception ex) {

        System.err.println("Error interno no controlado: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error inesperado en el servidor. Por favor, contacte al administrador."
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials() {
        ErrorCode code = ErrorCode.INVALID_CREDENTIALS;
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                code.getHttpStatus().value(),
                code.getHttpStatus().getReasonPhrase(),
                code.getDefaultMessage()
        );
        return new ResponseEntity<>(error, code.getHttpStatus());
    }

    @ExceptionHandler(org.springframework.security.authentication.DisabledException.class)
    public ResponseEntity<ErrorResponseDto> handleDisabledUser() {
        ErrorCode code = ErrorCode.EMAIL_UNVERIFIED;
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                code.getHttpStatus().value(),
                code.getHttpStatus().getReasonPhrase(),
                code.getDefaultMessage()
        );
        return new ResponseEntity<>(error, code.getHttpStatus());
    }
}
