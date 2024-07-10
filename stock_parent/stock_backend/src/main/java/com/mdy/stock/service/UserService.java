package com.mdy.stock.service;

import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.domain.UpdateRoleBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.viewObject.request.ReqListRoleVO;
import com.mdy.stock.viewObject.request.ReqListUserVO;
import com.mdy.stock.viewObject.request.ReqLoginVO;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;

import java.util.List;
import java.util.Map;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description 用户相关数据服务接口
 */

public interface UserService {
    /**
     * 根据用户名获取用户信息
     * @param name 用户名
     * @return
     */
    SysUser findUserByName(String name);

    /**
     * 获取验证码
     * @return
     */
    R<Map<String, String>> getCaptchaCode();

    /**
     * 根据分页参数查询用户数据
     *
     * @param reqListUserVO 前端传来的json数据
     * @return PageResult
     */
    R<PageResult<SysUser>> listUsers(ReqListUserVO reqListUserVO);

    /**
     * 添加用户信息
     * @param user 接受用户数据
     * @return 操作成功与否信息
     */
    boolean insertUser(SysUser user);

    /**
     * 根据分页信息查询用户角色信息
     * @return R
     */
    R<PageResult<SysRole>> listSysRoles(ReqListRoleVO reqListRoleVO);

    /**
     * 根据多个id批量删除用户信息
     * @param ids 存储待删除用户的id
     * @return 返回执行结果
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    R<RoleBO> getRolesById(Long userId);

    /**
     * 根据id修改它的角色信息
     * @param updateRoleBO 保持角色id和要改变的角色ids
     * @return 操作结果
     */
    boolean updateRolesById(UpdateRoleBO updateRoleBO);
}
