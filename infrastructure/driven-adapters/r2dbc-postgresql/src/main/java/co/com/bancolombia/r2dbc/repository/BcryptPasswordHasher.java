package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.login.gateways.PasswordHasher;
import co.com.bancolombia.model.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BcryptPasswordHasher implements PasswordHasher {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public boolean matches(CharSequence raw, String encoded) {
        return encoder.matches(raw, encoded);
    }

    @Override
    public Mono<User> verifyPassword(User user, String password) {
        if (!this.matches(password, user.getPassword())) {
            return Mono.error(new IllegalArgumentException("Invalid Credentials"));
        }
        return Mono.just(user);
    }

    @Override
    public String encode(CharSequence raw) {
        return encoder.encode(raw);
    }
}
