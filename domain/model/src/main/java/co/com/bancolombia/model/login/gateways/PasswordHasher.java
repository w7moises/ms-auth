package co.com.bancolombia.model.login.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface PasswordHasher {
    boolean matches(CharSequence raw, String encoded);

    Mono<User> verifyPassword(User user, String password);

    String encode(CharSequence raw);
}
