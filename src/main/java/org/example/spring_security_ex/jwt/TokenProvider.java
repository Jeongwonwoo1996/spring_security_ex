package org.example.spring_security_ex.jwt;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {
  private final SecretKey secretKey;
  private final long tokenValidityMilliSeconds;


  public TokenProvider(@Value("${jwt.secret}")String secret, @Value("${jwt.expiration-milliseconds}") long tokenValidityMilliSeconds){
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        SignatureAlgorithm.HS256.getJcaName());
    this.tokenValidityMilliSeconds = tokenValidityMilliSeconds;
  }
  // token build
  public String createJWT(Account account, Long expiredMs){
    return Jwts.builder()
        .claim("username", account.getUsername())
        .claim("name", account.getName())
        .claim("role", account.getAuthority())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+ expiredMs))
        .signWith(secretKey)
        .compact();

  }

  public String getUsername(String token){
    String username = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    return username;
  }

  public String getName(String token){
    String name = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("name", String.class);
    return name;
  }

  public Role getRole(String token){
    Role role = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", Role.class);
    return role;
  }

  // 발급받은 토큰 만료 여부 확인
  public Boolean isExpired(String token) {
    boolean isValid = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    return isValid;
  }
}
