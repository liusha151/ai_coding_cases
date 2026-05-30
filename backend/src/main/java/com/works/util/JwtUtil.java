package com.works.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

/**
 * JWT 工具类：生成和验证 JSON Web Token，用于前后端鉴权
 * Token 中携带 username 和 role 信息，有效期为 24 小时
 */
public class JwtUtil {
    private static final String SECRET = "WORKS_MGMT_SECRET_KEY_2026";
    private static final long EXPIRATION = 86400000L; // 24小时

    /** 生成 JWT Token，包含用户名和角色，设置过期时间 */
    public static String generateToken(String username, String role) {
        return JWT.create()
                .withClaim("username", username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    /** 验证 Token 有效性，返回解码后的 JWT 对象 */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /** 从 Token 中提取用户名 */
    public static String getUsername(String token) {
        return verifyToken(token).getClaim("username").asString();
    }

    /** 从 Token 中提取角色 */
    public static String getRole(String token) {
        return verifyToken(token).getClaim("role").asString();
    }
}
