package com.jackmu.jwt;

import com.jackmu.exception.SlowCapsuleAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {
    final Map<String, String> env = System.getenv();
    private String jwtSecret = env.get("jwt-secret");
    @Value("${app-jwt-expiration-milliseconds}")
    private int jwtExpirationDate;

    private static final Logger LOGGER = Logger.getLogger(JwtTokenProvider.class.getName());

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        LOGGER.info("This is the expiration date: " + expireDate.toString());


        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return jwtToken;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }
        catch (MalformedJwtException ex){
            throw new SlowCapsuleAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        }
        catch (ExpiredJwtException ex){
            throw new SlowCapsuleAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        }
        catch (UnsupportedJwtException ex){
            throw new SlowCapsuleAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        }
        catch (IllegalArgumentException ex){
            throw new SlowCapsuleAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }
}
