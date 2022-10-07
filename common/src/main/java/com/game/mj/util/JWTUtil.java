package com.game.mj.util;


import com.alibaba.fastjson.JSON;
import com.game.mj.exception.TokenException;
import com.game.mj.model.TokenBody;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private final static String TOKEN_SCRETE = "zhengScret";
    private final static Long TOKEN_EXPIRE = DateUtil.ONEDAY_MILLISECOND * 7;

    public static TokenBody getTokenBody(String token)throws TokenException {
        try {
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SCRETE).parseClaimsJws(token).getBody();
            String subject = claims.getSubject();
            TokenBody body = JSON.parseObject(subject, TokenBody.class);
            return body;
        }catch (Throwable e){
            e.printStackTrace();
            TokenException exception = new TokenException("error ",e);
            if (e instanceof ExpiredJwtException){
                exception.setExpired(true);
            }
            throw  exception;
        }
    }
    public static String getUserToken(String openId,long userId,long playerId,String serverId,String... params){
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        TokenBody body = new TokenBody(openId,userId,playerId,serverId,params);
        String subject = JSON.toJSONString(body);
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(nowMills))
                .setIssuedAt(now).setSubject(subject).signWith(algorithm,TOKEN_SCRETE);
        long expMills = nowMills + TOKEN_EXPIRE;
        Date expired = new Date(expMills);
        builder.setExpiration(expired);
        return builder.compact();

    }


}
