package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.login.gateways.TokenGenerator;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class H256TokenGenerator implements TokenGenerator {
    private final byte[] secretKey;
    private final String issuer;
    private final long ttlSeconds;
    private final String clientId;

    public H256TokenGenerator(
            @Value("${jwt.secret-base64}") String secretB64,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer,
            @Value("${security.jwt.ttl-seconds:3600}") long ttlSeconds,
            @Value("${spring.security.oauth2.resourceserver.jwt.client-id}") String clientId
    ) {
        this.secretKey = Base64.getDecoder().decode(secretB64);
        this.issuer = issuer;
        this.ttlSeconds = ttlSeconds;
        this.clientId = clientId;
    }

    @Override
    public String generate(String subject, List<String> roles, Long userId) {
        try {
            Instant now = Instant.now();
            var claims = new JWTClaimsSet.Builder()
                    .issuer(issuer)
                    .subject(subject)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
                    .claim("azp", clientId)
                    .claim("id", userId)
                    .claim("roles", roles)
                    .build();
            var header = new JWSHeader(JWSAlgorithm.HS256);
            var signed = new SignedJWT(header, claims);
            signed.sign(new MACSigner(secretKey));
            return signed.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Jwt Error Encoding", e);
        }
    }
}
