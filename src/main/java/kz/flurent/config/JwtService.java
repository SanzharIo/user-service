package kz.flurent.config;

import io.jsonwebtoken.*;
import kz.flurent.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtService {

    private long accessTokenValidity = 60*60*1000;

    private final String secret_key = "mysecretkey";

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

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

        String bearerToken = request.getHeader(TOKEN_HEADER);
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
