package co.com.bancolombia.model.login.gateways;

import java.util.List;

public interface TokenGenerator {
    String generate(String subject, List<String> roles, Long userId);
}
