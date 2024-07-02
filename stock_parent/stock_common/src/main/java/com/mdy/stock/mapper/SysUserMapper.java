package com.mdy.stock.mapper;

import com.mdy.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    List<SysUser> findAll();

    /**
     * 根据用户创建和更新数据的时间查询
     *
     * @param startTime 创建时间
     * @param endTime   最后更新时间
     * @param username 用户名
     * @param nickName 昵称
     * @return List<SysUser>
     */
    List<SysUser> findUserByStartAndEndTime(@Param("startTime")String startTime, @Param("endTime")String endTime,
                                            @Param("userName")String username, @Param("nickName")String nickName);
}
