package edu.uth.skincarebookingsystem.until;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private String secretKey;
    private long expiration;

    @Autowired
    public JwtUtils(
            @Value("${jwt.secret:ThisIsMySuperSecretKeyForJWT1234567890a}") String secretKey,
            @Value("${jwt.expiration:86400000}") long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new JwtException("Secret key length must be at least 256 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 👉 Method 1: Sửa logic để đảm bảo luôn có ROLE_
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        // Tự động thêm ROLE_ nếu chưa có (Fix lỗi người dùng truyền string "ADMIN")
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        claims.put("role", role);
        return createToken(claims, email);
    }

    // 👉 Method 2: Sửa logic generate từ UserDetails
    public String generateToken(UserDetails userDetails) {
        String email = userDetails.getUsername();

        // SỬA QUAN TRỌNG: Giữ nguyên authority, KHÔNG dùng substring cắt bỏ nữa
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return generateToken(email, roles);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}