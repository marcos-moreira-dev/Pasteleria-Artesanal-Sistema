package com.pasteleria.auth.application;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Encapsula la emision y validacion del token para no dispersar decisiones criptograficas.
 */
@Service
public class JwtTokenService {

  private final JwtProperties jwtProperties;
  private final SecretKey signingKey;

  public JwtTokenService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    String secret = jwtProperties.getSecret();

    if (!StringUtils.hasText(secret)) {
      throw new IllegalStateException("JWT_SECRET es obligatorio para iniciar el backend.");
    }

    if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
      throw new IllegalStateException("JWT_SECRET debe tener al menos 32 bytes para firmar tokens de forma segura.");
    }

    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Emite un token firmado con el rol actual para evitar consultas extra por request.
   */
  public String generateToken(String username, String role) {
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(jwtProperties.getExpirationSeconds());

    return Jwts.builder()
        .subject(username)
        .issuer(jwtProperties.getIssuer())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .claim("role", role)
        .signWith(signingKey)
        .compact();
  }

  /**
   * Valida firma y expiracion antes de exponer los claims al filtro de seguridad.
   */
  public Claims parseToken(String token) {
    return Jwts.parser()
        .verifyWith(signingKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Expone el TTL configurado para devolverlo tambien al frontend administrativo.
   */
  public long getExpirationSeconds() {
    return jwtProperties.getExpirationSeconds();
  }
}


