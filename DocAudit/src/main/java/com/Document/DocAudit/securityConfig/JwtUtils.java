package com.Document.DocAudit.securityConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final String SECRET_KEY = "f13523c8f8c92189702478021519d8eaa2d615a4f188081e193de290ff8c85cb";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username){
        // 86,400,000 milliseconds = 24 Hours
        long EXPIRATION_TIME = 86400000;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String GetUsernameFromToken(String token){
        return Jwts.parser().//Creates a parser to read and validate JWTs.
                setSigningKey(key)//Sets the secret key used to verify the token’s signature.
                .build()//Finalizes the parser configuration.
                /*
                Parses the token string into a Jws<Claims> object:
                  1.Splits into header, payload, signature.
                  2.Verifies the signature using your key.
                  3.Throws an exception if invalid/tampered.
                 */
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // Check if the token is mathematically valid and not expired
    public boolean ValidateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key) // Ensure this is the SAME key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
            return false;
        }
    }


}
