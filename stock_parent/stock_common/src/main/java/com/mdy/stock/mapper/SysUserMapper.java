package com.mdy.stock.mapper;

import com.mdy.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
* @author martin
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:14
* @Entity com.mdy.stock.pojo.entity.SysUser
*/

public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserByUserName(@Param("userName") String name);

}
