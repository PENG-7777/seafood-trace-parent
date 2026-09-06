package com.peng.admin.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 生成token、校验token、从token中获取存储的用户信息
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * 密钥，从application.yml读取
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * token过期时间，单位毫秒
     */
    @Value("${jwt.expire-time}")
    private Long expireTime;


    /**
     * 生成JWT token
     * @param userId 用户id（管理员id / 企业nodeId）
     * @param userType 用户类型 1捕捞2养殖3批发4零售商；0代表系统管理员
     * @return token字符串
     */
    public String createToken(Long userId, Integer userType) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("userType",userType);

        long now = System.currentTimeMillis();
        Date expireDate = new Date(now + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 校验token是否合法、是否过期
     * @param token jwt令牌
     * @return true合法；false失效/篡改
     */
    public boolean verifyToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("token已过期");
        } catch (MalformedJwtException e) {
            log.error("token格式错误，被篡改");
        } catch (Exception e) {
            log.error("token校验异常",e);
        }
        return false;
    }

    /**
     * 从token中获取用户ID
     * @param token jwt令牌
     * @return userId
     */
    public Long getUserId(String token){
        Claims claims = parseClaims(token);
        // get返回Object，强转为Long
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 从token获取用户类型
     * @param token jwt令牌
     * @return userType
     */
    public Integer getUserType(String token){
        Claims claims = parseClaims(token);
        return Integer.valueOf(claims.get("userType").toString());
    }

    /**
     * 解析token拿到负载Claims
     * @param token token字符串
     * @return Claims jjwt的负载对象
     */
    private Claims parseClaims(String token){
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }
}
