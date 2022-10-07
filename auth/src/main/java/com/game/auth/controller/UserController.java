package com.game.auth.controller;

import com.game.mj.constant.InfoConstant;
import com.game.mj.exception.TokenException;
import com.game.mj.model.ResponseVo;
import com.game.mj.model.TokenBody;
import com.game.mj.util.DigestUtil;
import com.game.mj.util.JWTUtil;
import com.game.infrustructure.redis.JsonRedisManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author zheng
 */
@RestController("/user")
@Api("用户登陆校验")
public class UserController {

    @Autowired
    private JsonRedisManager jsonRedisManager;
    @GetMapping("/userLogin")
    @ResponseBody
    @ApiOperation("用户登录鉴权")
    public ResponseVo login(@RequestParam String userName, @RequestParam String password ){
        String pass = "abcd";
        if (!DigestUtil.matches(pass,DigestUtil.encodePass(password))){
            return ResponseVo.fail("password not wright");
        }
        String refresh = UUID.randomUUID().toString().replace("-","");
        String token = JWTUtil.getUserToken(userName, 1234l, 12345l, "1", "test");
        jsonRedisManager.setObjectHash1(refresh,InfoConstant.TOKEN,token);
        jsonRedisManager.setObjectHash1(refresh,InfoConstant.USER_NAME,userName);
        jsonRedisManager.setObjectHash1(refresh,InfoConstant.ROLES,"admin,query");

        return ResponseVo.success(token);
    }

    @GetMapping("/validata")
    @ResponseBody
    @ApiOperation("校验token")
    public ResponseVo validateToken(@RequestParam String  token){
        try {
            TokenBody tokenBody = JWTUtil.getTokenBody(token);
            return ResponseVo.success(tokenBody);
        } catch (TokenException e) {
            e.printStackTrace();
            return ResponseVo.fail("token not wright");
        }
    }
    @GetMapping("/refresh")
    @ResponseBody
    @ApiOperation("刷新token")
    public ResponseVo refreshToken(@RequestParam String refreshToken,@RequestParam String userName){
        String token = JWTUtil.getUserToken(userName, 1234l, 12345l, "1", "test");
        jsonRedisManager.setObjectHash1(refreshToken,InfoConstant.TOKEN,token);
        jsonRedisManager.setObjectHash1(refreshToken,InfoConstant.USER_NAME,userName);
        jsonRedisManager.setObjectHash1(refreshToken,InfoConstant.ROLES,"admin,query");
        return ResponseVo.success("hello");
    }
}
