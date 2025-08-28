package co.com.bancolombia.model.login.gateways;

public interface PasswordHasher {
    boolean matches(CharSequence raw, String encoded);

    String encode(CharSequence raw);
}
