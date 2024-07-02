package com.mdy.stock.controller;

import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.impl.UserServiceImpl;
import com.mdy.stock.viewObject.request.ReqListRoleVO;
import com.mdy.stock.viewObject.request.ReqListUserVO;
import com.mdy.stock.viewObject.request.ReqLoginVO;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description
 */

@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @Resource
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
    private R<RespLoginVo> login(@RequestBody ReqLoginVO reqLoginVo) {
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

    /**
     * 根据分页参数查询用户数据
     * @param reqListUserVO 前端传来的json数据
     * @return PageResult
     */
    @PostMapping("/users")
    private R<PageResult<SysUser>> listUsers(@RequestBody ReqListUserVO reqListUserVO) {
        return userService.listUsers(reqListUserVO);
    }

    /**
     * 添加用户信息
     * @param user 接受用户数据
     * @return 操作成功与否信息
     */
    @PostMapping("/user")
    private R<String> insertUsers(@RequestBody SysUser user) {
        if (userService.insertUser(user)) {
            return R.ok("操作成功");
        }
        return R.error("操作失败");
    }

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    @GetMapping("/user/roles/{userId}")
    private R<RoleBO> getUserRoles(@PathVariable("userId") Long userId) {
        return userService.getRolesById(userId);
    }

    /**
     * 根据分页信息查询用户角色信息
     * @return R
     */
    @PostMapping("/roles")
    private R<PageResult<SysRole>>  listSysRoles(@RequestBody ReqListRoleVO reqListRoleVO) {
        return userService.listSysRoles(reqListRoleVO);
    }

    /**
     * 根据多个id批量删除用户信息
     * @param ids 存储待删除用户的id
     * @return 返回执行结果
     */
    @DeleteMapping("/user")
    private R<String> deleteUsers(@RequestBody List<Long> ids) {
        if (userService.deleteByIds(ids)) {
            return R.ok("操作成功");
        }
        return R.error("操作失败");
    }
}
