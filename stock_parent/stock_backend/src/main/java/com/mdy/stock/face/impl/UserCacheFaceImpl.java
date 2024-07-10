package com.mdy.stock.face.impl;

import com.mdy.stock.face.UserCacheFace;
import com.mdy.stock.mapper.SysPermissionMapper;
import com.mdy.stock.mapper.SysRoleMapper;
import com.mdy.stock.pojo.domain.SysPermissionBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.security.detail.LoginUserDetail;
import com.mdy.stock.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mdy
 * @date 2024-07-10 8:20
 * @description
 */
@Component
@CacheConfig(cacheNames = "role")
public class UserCacheFaceImpl implements UserCacheFace {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private UserServiceImpl userService;

    /**
     * 组装menus，type的值决定本次递归组装的层级
     * @param permissions 权限集合
     * @param type 当前应组装的权限的层级
     * @return 返回组装好的menus
     */
    private List<SysPermissionBO> composeMenus(List<SysPermissionBO> permissions, List<SysPermissionBO> menus, Integer type) {
        if (type >= 3) {
            return menus;
        }

        // 用一个map记录多个parentId和type都相同的permission
        Map<Long, List<SysPermissionBO>> childrenMap = new HashMap<>();
        for (SysPermissionBO permission : permissions) {
            if (Objects.equals(permission.getType(), type)) {
                permission.setChildren(new ArrayList<>());
                if (childrenMap.containsKey(permission.getParentId())) {
                    childrenMap.get(permission.getParentId()).add(permission);
                } else {
                    List<SysPermissionBO> children = new ArrayList<>();
                    children.add(permission);
                    childrenMap.put(permission.getParentId(), children);
                }
            }
        }

        // 将组装好的子集合，按照parentId分别插入到对应的父结点中
        if (menus.isEmpty()) {
            menus.addAll(childrenMap.get(0L));
        } else {
            for (SysPermissionBO menu : menus) {
                // childrenMap中有和menu的id相等的key，说明该键值对的键值是这个menu的子集
                if (childrenMap.containsKey(menu.getId())) {
                    menu.setChildren(childrenMap.get(menu.getId()));
                }
            }
        }

        return composeMenus(permissions, menus, type + 1);
    }

    /**
     * 将用户角色信息查询并计入缓存
     * @return 返回用户权限数据
     */
    @Cacheable(key = "#root.methodName", unless = "#result != null")
    @Override
    public List<SysRole> listRolesToCache() {
        return sysRoleMapper.findAll();
    }

    /**
     * 获取用户权限信息并计入缓存
     * @param userName 用户名
     * @return 用户权限信息
     */
    @Cacheable(key = "#userName")
    @Override
    public UserDetails getLoginUserDetail(String userName) {
        SysUser user = userService.findUserByName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 获取用户所拥有的权限集合
        List<SysPermissionBO> permissions = sysPermissionMapper.findUserPermissions(user.getId());

        // 将权限按照层级顺序组装到ResLoginVO的children属性中
        List<SysPermissionBO> menus = new ArrayList<>();
        // 形成菜单栏层级
        composeMenus(permissions, menus, 1);

        // 获取用户拥有的角色
        List<SysRole> roles = sysRoleMapper.findRolesById(user.getId());

        // 将用户的权限标识和角色标识维护到权限集合中
        List<String> permissionsAndRoles = new ArrayList<>();
        permissions.forEach(per -> {
            if (StringUtils.isNotBlank(per.getPerms())) {
                permissionsAndRoles.add(per.getPerms());
            }
        });
        roles.forEach(role -> {
            // 必须要在权限前加"ROLE_"字符串，这是SpringSecurity的规定！
            permissionsAndRoles.add("ROLE_" + role.getName());
        });

        List<GrantedAuthority> authorityList = AuthorityUtils
                .createAuthorityList(permissionsAndRoles.toArray(new String[0]));

        // 获取用户可用的按钮集合
        List<String> buttonPermissions = new ArrayList<>();
        for (SysPermissionBO sysPermission: permissions) {
            if (Objects.equals(sysPermission.getType(), 3)) {
                buttonPermissions.add(sysPermission.getButtonName());
            }
        }

        // 组装并返回登录用户信息
        LoginUserDetail loginUserDetail = new LoginUserDetail();
        BeanUtils.copyProperties(user, loginUserDetail);
        loginUserDetail.setMenus(menus);
        loginUserDetail.setAuthorities(authorityList);
        loginUserDetail.setPermissions(buttonPermissions);

        return loginUserDetail;
    }

}
