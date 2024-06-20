package com.mdy.stock.controller;

import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.impl.UserServiceImpl;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description
 */

@RestController
@RequestMapping("/api")
public class userController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 单例测试
     * @param name 用户名
     * @return 用户数据
     */
    @GetMapping("/user/{username}")
    private SysUser getUserByName(@PathVariable("username") String name) {
        System.out.println(passwordEncoder.encode(name));
        return userService.getInfoByUsername(name);
    }

    /**
     * 用户登录功能
     * @param reqLoginVo 封装的请求对象
     * @return 登录信息
     */
    @PostMapping("/login")
    private R<RespLoginVo> login(@RequestBody ReqLoginVo reqLoginVo) {
        return userService.login(reqLoginVo);

    }

    /**
     * 生成验证码
     * @return 验证码
     */
    @GetMapping("/captcha")
    private R<Map> getCaptchaCode() {
        return userService.getCaptchaCode();
    }
}
