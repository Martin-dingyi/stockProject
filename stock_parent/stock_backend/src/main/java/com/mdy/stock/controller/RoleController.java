package com.mdy.stock.controller;

/**
 * @author mdy
 * @date 2024-07-08 4:41
 * @description 角色控制器
 */

import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.domain.UpdateRoleBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.service.impl.UserServiceImpl;
import com.mdy.stock.viewObject.request.ReqListRoleVO;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Resource
    private UserServiceImpl userService;

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    @GetMapping("/user/roles/{userId}")
    public R<RoleBO> getUserRoles(@PathVariable("userId") Long userId) {
        return userService.getRolesById(userId);
    }

    /**
     * 根据分页信息查询用户角色信息
     * @return R
     */
    @PostMapping("/roles")
    public R<PageResult<SysRole>>  listSysRoles(@RequestBody ReqListRoleVO reqListRoleVO) {
        return userService.listSysRoles(reqListRoleVO);
    }

    /**
     * 根据id修改它的角色信息
     * @param updateRoleBO 保持角色id和要改变的角色ids
     * @return 操作结果
     */
    @PutMapping("/user/roles")
    public R<String> updateUserRoles(@RequestBody UpdateRoleBO updateRoleBO) {
        if (userService.updateRolesById(updateRoleBO)) {
            return R.ok("操作成功");
        }
        return R.error("操纵失败");
    }
}
