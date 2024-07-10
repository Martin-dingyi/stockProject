package com.mdy.stock.face;

import com.mdy.stock.pojo.entity.SysRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author mdy
 * @date 2024-07-10 8:10
 * @description
 */
public interface UserCacheFace {

    /**
     * 将用户角色信息查询并计入缓存
     * @return 返回用户权限数据
     */
    List<SysRole> listRolesToCache();

    /**
     * 获取用户权限信息并计入缓存
     * @param userName 用户名
     * @return 用户权限信息
     */
    UserDetails getLoginUserDetail(String userName);

}
