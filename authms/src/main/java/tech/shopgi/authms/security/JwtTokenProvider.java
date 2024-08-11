package tech.shopgi.authms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.shopgi.authms.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecretKey;
    private final Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("Auth API ShopGi")
                    .withClaim("roles", user.getRole())
                    .withSubject(user.getUsername())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating JWT token.");
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("Auth API ShopGi")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired JTW token.");
        }
    }

    public Boolean validateToken(String token) {
        try {
            JWT.require(algorithm)
                    .withIssuer("Auth API ShopGi")
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Instant expirationDate() {
        return Instant.now().plus(8, ChronoUnit.HOURS);
    }
}