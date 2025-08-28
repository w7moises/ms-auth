package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.login.gateways.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordHasher implements PasswordHasher {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public boolean matches(CharSequence raw, String encoded) {
        return encoder.matches(raw, encoded);
    }

    @Override
    public String encode(CharSequence raw) {
        return encoder.encode(raw);
    }
}
