package co.com.bancolombia.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> attrs = super.getErrorAttributes(
                request,
                options.including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS)
        );
        Throwable error = getError(request);

        if (error instanceof ConstraintViolationException cve) {
            attrs.put("status", 400);
            attrs.put("error", "Bad Request");
            attrs.put("message", "Validation failed");
            attrs.put("errors", cve.getConstraintViolations().stream()
                    .map(v -> Map.of(
                            "field", v.getPropertyPath().toString(),
                            "message", v.getMessage()))
                    .toList());
        } else if (error instanceof WebExchangeBindException web) {
            attrs.put("status", 400);
            attrs.put("error", "Bad Request");
            attrs.put("message", "Binding failed");
            attrs.put("errors", web.getFieldErrors().stream()
                    .map(fe -> {
                        assert fe.getDefaultMessage() != null;
                        assert fe.getRejectedValue() != null;
                        return Map.of(
                                "field", fe.getField(),
                                "message", fe.getDefaultMessage());
                    })
                    .toList());
        }
        return attrs;
    }
}
