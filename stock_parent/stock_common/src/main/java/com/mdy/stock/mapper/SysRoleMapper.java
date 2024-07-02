package com.mdy.stock.mapper;

import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author martin
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 查询所有用户角色信息
     * @return R
     */
    List<SysRole> findAll();

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    List<SysRole> findRolesById(@Param("userId") Long userId);
}
