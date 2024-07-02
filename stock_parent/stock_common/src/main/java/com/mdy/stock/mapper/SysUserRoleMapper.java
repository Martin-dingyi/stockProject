package com.mdy.stock.mapper;

import com.mdy.stock.pojo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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

    /**
     * 增加一条用户和角色的对应关系
     * @param l id
     * @param userId 用户id
     * @param id 角色id
     * @param date 日期时间
     * @return 执行结果
     */
    int insertUserRoles(@Param("id") long l, @Param("userId")Long userId, @Param("roleId")Long id,
                        @Param("date")Date date);

    void deleteByUsrId(@Param("userId") Long userId);
}
