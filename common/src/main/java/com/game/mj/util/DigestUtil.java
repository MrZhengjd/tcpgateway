package com.game.mj.util;

import org.springframework.util.DigestUtils;

/**
 * @author zheng
 */
public class DigestUtil {
    public static boolean matches(CharSequence password,String encodePassword){
        return encodePassword.equals(DigestUtils.md5DigestAsHex(password.toString().getBytes()));
    }

    public static String encodePass(CharSequence charSequence){
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
    }
}
