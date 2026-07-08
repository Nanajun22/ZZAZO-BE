package org.example.zzazo.global.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 액세스/리프레시 토큰 발급 담당
@Component
public class JwtProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 액세스 토큰 발급
    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, accessTokenExpiration);
    }

    // 리프레시 토큰 발급
    public String createRefreshToken(Long userId) {
        return createToken(userId, null, refreshTokenExpiration);
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    private String createToken(Long userId, String email, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key);

        if (email != null) {
            builder.claim("email", email);
        }

        return builder.compact();
    }
}
