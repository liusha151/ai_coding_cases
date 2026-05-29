package com.works.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "WORKS_MGMT_SECRET_KEY_2026";
    private static final long EXPIRATION = 86400000L;

    public static String generateToken(String username, String role) {
        return JWT.create()
                .withClaim("username", username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    public static String getUsername(String token) {
        return verifyToken(token).getClaim("username").asString();
    }

    public static String getRole(String token) {
        return verifyToken(token).getClaim("role").asString();
    }
}
