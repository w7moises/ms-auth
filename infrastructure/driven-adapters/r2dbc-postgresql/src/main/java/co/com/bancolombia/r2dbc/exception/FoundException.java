package co.com.bancolombia.r2dbc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FoundException extends ResponseStatusException {
    public FoundException(String message) {
        super(HttpStatus.FOUND, message);
    }
}