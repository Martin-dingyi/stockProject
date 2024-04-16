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

    @GetMapping("/user/{username}")
    private SysUser getUserByName(@PathVariable("username") String name) {
        System.out.println(passwordEncoder.encode(name));
        return userService.getInfoByUsername(name);
    }

    /**
     * 用户登录功能
     * @param reqLoginVo
     * @return
     */
    @PostMapping("/login")
    private R<RespLoginVo> login(@RequestBody ReqLoginVo reqLoginVo) {
        return userService.login(reqLoginVo);

    }

    // 设置当前请求方法为DELETE，表示REST风格中的删除操作
    @DeleteMapping(value = "/users/{id}/{name}")
    public String delete(@PathVariable Integer id, @PathVariable("name") String username) {
        System.out.println("user id:" + id);
        System.out.println("username:" + username);
        return "{'module':'user delete'}";
    }
}
