package tech.shopgi.customerms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.ClaimsHolder;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${api.jwt.token.secret}")
    private String jwtSecretKey;
    private final String jwtIssuer = "Auth API ShopGi";

    public DecodedJWT getDecodedJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
        return JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .build()
                .verify(token);
    }

    public Boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getDecodedJwt(token).getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJwt(token);
        return decodedJWT.getClaim("roles")
                .asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
