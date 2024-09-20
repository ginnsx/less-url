package com.github.xioshe.less.url.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception,
                                                       HttpServletResponse response) {
        log.debug("occur AuthenticationException: ", exception);
        log.info("AuthenticationException: {}", exception.getMessage());
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        ProblemDetail errorDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "Full authentication is required to access this resource");
        return errorDetail;
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ProblemDetail handleUrlNotFoundException(UrlNotFoundException exception) {
        log.debug("occur AuthenticationException: ", exception);
        log.info("UrlNotFoundException: {}", exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setProperty("description", "The short url is not found");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {

        ProblemDetail errorDetail = null;

        log.error("occur exception: ", exception);

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException || exception instanceof JwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }

        return errorDetail;
    }
}
