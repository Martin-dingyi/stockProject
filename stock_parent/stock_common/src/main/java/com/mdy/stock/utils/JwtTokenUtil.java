package com.mdy.stock.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Piconjo
 * @date 2020/5/19  16:38
 */
public class JwtTokenUtil {
    // 请求头中Token的key
    public static final String TOKEN_HEADER = "authorization";
    // 签名主题
    public static final String SUBJECT = "JRZS";
    // 过期时间，单位ms。默认为7天
    public static final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;
    // 应用密钥，默认为abcdrfg。注意不得低于4位
    public static final String APPSERVER_KEY = "abcdrfg";
    // 角色权限声明
    private static final String ROLE_CLAIMS = "role";

    /**
     * 生成Token
     * @param username 用户名
     * @param role 该用户所拥有的角色
     * @return Token字符串
     */
    public static String createToken(String username, String role) {
        Map<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);

        return Jwts
                .builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, APPSERVER_KEY).compact();
    }

    /**
     * 反解析Token
     * @param token token字符串
     * @return 解析结果封装在Claims中
     */
    public static Claims checkJwt(String token) {
        return Jwts.parser().setSigningKey(APPSERVER_KEY).parseClaimsJws(token).getBody();
    }


    /**
     * 从Token中获取用户名
     * @param token token字符串
     * @return 用户名
     */
    public static String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSERVER_KEY).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    /**
     * 从Token中获取用户角色
     * @param token token字符串
     * @return 用户的角色
     */
    public static String getUserRole(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSERVER_KEY).parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    /**
     * 校验Token是否过期
     * @param token token字符串
     * @return 结果
     */
    public static boolean isExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(APPSERVER_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
