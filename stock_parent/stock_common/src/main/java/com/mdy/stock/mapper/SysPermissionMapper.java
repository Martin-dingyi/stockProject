package com.mdy.stock.mapper;

import com.mdy.stock.pojo.domain.SysPermissionBO;
import com.mdy.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author martin
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 获取用户所拥有的权限
     * @param id 用户id
     * @return 权限集合
     */
    List<SysPermissionBO> findUserPermissions(@Param("userId")Long id);
}
