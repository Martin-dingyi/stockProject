package com.mdy.stock.mapper;

import com.mdy.stock.pojo.entity.SysUserRole;

/**
* @author martin
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:14
* @Entity com.mdy.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}
