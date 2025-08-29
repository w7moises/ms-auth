package co.com.bancolombia.r2dbc.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends I18nResponseStatusException {
    public InvalidCredentialsException(String code, Object... args) {
        super(HttpStatus.UNAUTHORIZED, code, args);
    }
}