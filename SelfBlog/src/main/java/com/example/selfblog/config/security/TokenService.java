package com.example.selfblog.config.security;

import com.example.selfblog.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenService {
    private static final String SUB = "sub";

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SUB, user.getId());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 3600000L))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }
    public Integer getUserIdFromToken(String token) {
        String userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            return null;
        }
        return Integer.valueOf(userId);
    }
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey ("secret")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }
    public boolean validateToken(String token,User user){
        Integer userId = getUserIdFromToken(token);
        return userId.equals(user.getId())
                && !isTokenExpired(token);
    }
    public boolean isTokenExpired (String token) {
        Date expireDate = getExpireDateFromToken(token);
        return expireDate.before(new Date());
    }
    /**
     *从令牌中获取过期时间
     *@param token JWT字符串*@return过期时间
     */
    public Date getExpireDateFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
}

