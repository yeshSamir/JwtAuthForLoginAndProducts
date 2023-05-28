package com.jwtauth.util;

import com.jwtauth.entity.Users;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenUtils {
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(JwtTokenUtils.class));
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;
    private String secretKey = "ababababababa";

    public String generateAccessToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail() + " " + user.getId())
                .setIssuer("java")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public boolean validateJsonToke(String value) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(value);
            return true;

        } catch (ExpiredJwtException e) {

            LOGGER.info("expired");
        } catch (IllegalArgumentException e) {

            LOGGER.info("expired");
        } catch (MalformedJwtException e) {

            LOGGER.info("expired");
        } catch (UnsupportedJwtException e) {

            LOGGER.info("expired");
        }

        return false;
    }

    public String getSubjet(String token){
        return parseClaim(token).getSubject();
    }

    private Claims parseClaim(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJwt(token)
                .getBody();
    }
}
