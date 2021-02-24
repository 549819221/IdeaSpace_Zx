package com.server.ftpsynchostel.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    //过期时间15分钟
    private static final int EXPIRE_TIME = 120 * 60 * 1000;
    // token私钥
    private static final String TOKEN_SECRET = "a276a92e0bf2413483f3e50a2c7ccd7d";

    /**
     * @description  生成token
     * @param  account  账号
     * @param  password  密码
     * @return
     * @date  20/08/12 17:16
     * @author  wanghb
     * @edit
     */
    public static String sign(String account, String password) {
        //过期时间
        Date date = new Date( System.currentTimeMillis() + EXPIRE_TIME );
        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256( TOKEN_SECRET );
        //设置头部信息
        Map<String, Object> header = new HashMap( 2);
        header.put( "typ", "JWT" );
        header.put( "alg", "HS256" );
        //附带username,userId信息，生成签名
        return JWT.create()
                .withHeader( header )
                .withClaim( "account", account )
                .withExpiresAt( date )
                .sign( algorithm );
    }


    /**
     * @description  校验token是否正确
     * @param  token
     * @return  返回结果
     * @date  20/08/12 17:16
     * @author  wanghb
     * @edit
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256( TOKEN_SECRET );
            JWTVerifier verifier = JWT.require( algorithm ).build();
            DecodedJWT jwt = verifier.verify( token );
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
