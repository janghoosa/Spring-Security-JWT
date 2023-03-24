package com.example.springsecurityjwt.common.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  private static final String SECRET_KEY = "MTI0MTRzZWNyZXRrZXlpc3NlY3JldDEyMzQxNDEyNGdhZ3dhZ2E=";
  private static final long EXPIRATION_TIME = 86400000;
  private static final byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
  private static final Key key = Keys.hmacShaKeyFor(keyBytes);

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now)
        .setExpiration(expirationDate).signWith(
            key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
        .getSubject();
  }

  private boolean isTokenExpired(String token) {
    Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private Date getExpirationDateFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
        .getExpiration();
  }
}
