package kz.flurent.config;

import io.jsonwebtoken.*;
import kz.flurent.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtService {

    @Value("${access.time}")
    private long accessTokenValidity;

    @Value("${access.key}")
    private String secret_key;

    public String createToken(User user) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        Claims claims = Jwts
                .claims()
                .subject(user.getUsername())
                .add("firstName",user.getFirstName())
                .add("lastName",user.getLastName())
                .build();

        return Jwts.builder()
                .claims(claims)
                .expiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS512, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(secret_key).build();
        return jwtParser.parseClaimsJwt(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String TOKEN_HEADER = "Authorization";
        String bearerToken = request.getHeader(TOKEN_HEADER);
        String TOKEN_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
}
