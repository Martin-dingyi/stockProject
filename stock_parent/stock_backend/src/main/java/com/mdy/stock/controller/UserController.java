package com.mdy.stock.controller;

import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.domain.UpdateRoleBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.impl.UserServiceImpl;
import com.mdy.stock.viewObject.request.ReqListRoleVO;
import com.mdy.stock.viewObject.request.ReqListUserVO;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private PasswordEncoder passwordEncoder;

    /**
     * 生成验证码
     * @return 验证码
     */
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode() {
        return userService.getCaptchaCode();
    }

    /**
     * 根据分页参数查询用户数据
     * @param reqListUserVO 前端传来的json数据
     * @return PageResult
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/users")
    public R<PageResult<SysUser>> listUsers(@RequestBody ReqListUserVO reqListUserVO) {
        return userService.listUsers(reqListUserVO);
    }

    /**
     * 添加用户信息
     * @param user 接受用户数据
     * @return 操作成功与否信息
     */
    @PreAuthorize("hasAnyAuthority('sys:user:add')")
    @PostMapping("/user")
    public R<String> insertUsers(@RequestBody SysUser user) {
        if (userService.insertUser(user)) {
            return R.ok("操作成功");
        }
        return R.error("操作失败");
    }

    /**
     * 根据多个id批量删除用户信息
     * @param ids 存储待删除用户的id
     * @return 返回执行结果
     */
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    @DeleteMapping("/user")
    public R<String> deleteUsers(@RequestBody List<Long> ids) {
        if (userService.deleteByIds(ids)) {
            return R.ok("操作成功");
        }
        return R.error("操作失败");
    }

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return R
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/user/info/{userId}")
    public R<SysUser> getUserInfo(@PathVariable("userId") Long userId) {
        // 太简单，不用做过多练习
        return null;
    }

    /**
     * 更新用户信息
     * @param user 接受要更新的用户数据
     * @return 执行结果
     */
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    @PutMapping("/user")
    public R<String> updateUsers(@RequestBody SysUser user) {
        // 太简单，不用做过多练习
        return null;
    }

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    @GetMapping("/user/roles/{userId}")
    public R<RoleBO> getUserRoles(@PathVariable("userId") Long userId) {
        return userService.getRolesById(userId);
    }

    /**
     * 根据分页信息查询用户角色信息
     * @return R
     */
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    @PostMapping("/roles")
    public R<PageResult<SysRole>>  listSysRoles(@RequestBody ReqListRoleVO reqListRoleVO) {
        return userService.listSysRoles(reqListRoleVO);
    }

    /**
     * 根据id修改它的角色信息
     * @param updateRoleBO 保持角色id和要改变的角色ids
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('sys:role:update')")
    @PutMapping("/user/roles")
    public R<String> updateUserRoles(@RequestBody UpdateRoleBO updateRoleBO) {
        if (userService.updateRolesById(updateRoleBO)) {
            return R.ok("操作成功");
        }
        return R.error("操纵失败");
    }
}
