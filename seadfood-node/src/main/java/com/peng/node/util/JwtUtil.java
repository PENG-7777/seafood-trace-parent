package com.peng.node.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 生成、解析、校验流通节点企业用户token
 * payload存放：nodeId 企业编号，nodeType 企业类型
 */
@Component
public class JwtUtil {

    /**
     * jwt密钥，读取yml配置 jwt.secret
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * token过期时间，单位毫秒，读取yml配置 jwt.expire-time
     */
    @Value("${jwt.expire-time}")
    private Long expireTime;

    /**
     * 获取加密密钥
     * @return SecretKey
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成企业用户JWT token
     * @param nodeId 企业id node_info主键
     * @param nodeType 企业类型 1捕捞 2养殖 3冷冻加工 4批发商 5零售商
     * @return token字符串
     */
    public String generateToken(Integer nodeId, Integer nodeType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nodeId", nodeId);
        claims.put("nodeType", nodeType);

        // 过期时间 = 当前时间 + 配置过期时长
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token，获取Claims载荷
     * @param token jwt令牌
     * @return Claims 载荷；token非法/过期返回null
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // token过期
            return null;
        } catch (JwtException e) {
            // token篡改、格式错误
            return null;
        }
    }

    /**
     * 校验token是否有效
     * @param token jwt令牌
     * @return true有效；false失效/过期/篡改
     */
    public boolean validateToken(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims == null) {
            return false;
        }
        // 判断是否已经过期
        return !claims.getExpiration().before(new Date());
    }

    /**
     * 从token中获取企业nodeId
     * @param token jwt令牌
     * @return 企业id，token无效返回null
     */
    public Integer getNodeId(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims == null) {
            return null;
        }
        return (Integer) claims.get("nodeId");
    }

    /**
     * 从token中获取企业类型 nodeType
     * @param token jwt令牌
     * @return 企业类型，token无效返回null
     */
    public Integer getNodeType(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims == null) {
            return null;
        }
        return (Integer) claims.get("nodeType");
    }
}
