package com.mos.backend.common.handler.exeption;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mos.backend.common.exception.ClientException;
import com.mos.backend.common.exception.ErrorCode;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.users.entity.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MosException.class)
    public ResponseEntity<String> handleMosException(MosException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getStatus();
        String message = errorCode.getMessage(messageSource);
        return ResponseEntity
                .status(status)
                .body(message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(UserErrorCode.USER_FORBIDDEN.getStatus())
                .body(UserErrorCode.USER_FORBIDDEN.getMessage(messageSource));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ObjectError first = e.getBindingResult().getAllErrors().stream().findFirst().get();
        String message = first.getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handleClientException(ClientException e) {
        String message = e.getMessage();
        HttpStatus status = e.getStatus();

        return ResponseEntity
                .status(status)
                .body(message);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<String> handleJWTVerificationException(JWTVerificationException e) {
        String message = "토큰이 유효하지 않습니다.";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }
}
