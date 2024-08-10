package tech.shopgi.authms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.shopgi.authms.model.User;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key KEY;
    private final Long JWT_EXPIRATION = 28800000L;  // 8 horas

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        this.KEY = new SecretKeySpec(decodedKey, 0, decodedKey.length, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getRole());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
