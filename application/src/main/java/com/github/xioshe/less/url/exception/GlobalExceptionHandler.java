package com.github.xioshe.less.url.exception;

import com.github.xioshe.api.response.model.Result;
import com.github.xioshe.less.url.rate_limiting.exception.RateLimitException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.PrematureJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public Result handleAuthenticationException(AuthenticationException exception,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        log.debug("occur AuthenticationException: ", exception);
        log.warn("AuthenticationException in path {}: {}", request.getRequestURI(), exception.getMessage());
        ProblemDetail errorDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "Full authentication is required to access this resource");

        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        return Result.failure(HttpStatus.UNAUTHORIZED.value(),
                "Full authentication is required to access this resource", errorDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException exception,
                                                     HttpServletRequest request) {
        log.debug("occur AccessDeniedException: ", exception);
        log.warn("AccessDeniedException in path {} : {}", request.getRequestURI(), exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        problemDetail.setProperty("description", "You are not authorized to access this resource");
        return Result.failure(HttpStatus.FORBIDDEN.value(),
                "You are not authorized to access this resource", problemDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result handleBadCredentialsException(BadCredentialsException exception) {
        log.error("occur BadCredentialsException: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The username or password is incorrect");
        return Result.failure(HttpStatus.UNAUTHORIZED.value(),
                "The username or password is incorrect", errorDetail);
    }

    @ExceptionHandler(AccountStatusException.class)
    public Result handleAccountStatusException(AccountStatusException exception) {
        log.error("occur AccountStatusException: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        errorDetail.setProperty("description", "The account is locked");
        return Result.failure(HttpStatus.FORBIDDEN.value(), "The account is locked", errorDetail);
    }

    @ExceptionHandler({SignatureException.class, JwtException.class})
    public Result handleJwtException(Exception exception) {
        log.error("occur JWT Exception: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT signature is invalid");
        return Result.failure(HttpStatus.UNAUTHORIZED.value(),
                "The JWT signature is invalid", errorDetail);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleExpiredJwtException(ExpiredJwtException exception) {
        log.error("occur ExpiredJwtException: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT token has expired");
        return Result.failure(HttpStatus.UNAUTHORIZED.value(), "The JWT token has expired", errorDetail);
    }

    @ExceptionHandler(PrematureJwtException.class)
    public Result handlePrematureJwtException(PrematureJwtException exception) {
        log.error("occur PrematureJwtException: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT token has not become valid yet");
        return Result.failure(HttpStatus.UNAUTHORIZED.value(), "The JWT token has not become valid yet", errorDetail);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public Result handleUrlNotFoundException(UrlNotFoundException exception, HttpServletRequest request) {
        log.debug("occur UrlNotFoundException: ", exception);
        log.warn("UrlNotFoundException: {} because of {}", request.getRequestURI(), exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setProperty("description", "The short url is not found");
        return Result.failure(5404, "The short url is not found", problemDetail);
    }

    @ExceptionHandler(DistributedLockException.class)
    public Result handleDistributedLockException(DistributedLockException exception, HttpServletRequest request) {
        log.debug("occur DistributedLockException: ", exception);
        log.warn("DistributedLockException: {} because of {}", request.getRequestURI(), exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setProperty("description", "Concurrent operation exception");
        return Result.failure(5405, "Concurrent operation exception", problemDetail);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitException(RateLimitException exception,
                                                                        HttpServletRequest request) {
        log.debug("occur RateLimitException: ", exception);
        log.warn("RateLimitException in path {}: {}", request.getRequestURI(), exception.getMessage());
        var errorMessage = buildErrorMessage(exception);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(errorMessage);
    }

    private Map<String, Object> buildErrorMessage(RateLimitException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 429);
        errorResponse.put("message", "请求太频繁，请稍后重试");
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("detail", exception.getMessage());

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    public Result handleGenericException(Exception exception) {
        log.error("occur unexpected exception: ", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        errorDetail.setProperty("description", "Unknown internal server error.");
        var msg = exception.getMessage();
        if (msg == null) {
            msg = "未知异常";
        }
        return Result.failure(5000, msg, errorDetail);
    }
}
