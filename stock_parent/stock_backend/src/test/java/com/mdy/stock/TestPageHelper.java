package com.mdy.stock;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.viewObject.response.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author mdy
 * @date 2024-06-21 0:27
 * @description 测试分页插件
 */

@SpringBootTest
public class TestPageHelper {
    @Autowired
    SysUserMapper sysUserMapper;
    @Test
    public void test() {
        int pageSize = 10;  // 每页大小（行数）
        int page = 2; // 当前页
        PageHelper.startPage(page, pageSize);
        List<SysUser> sysUsers = sysUserMapper.findAll();
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);
        System.out.println(pageInfo);
    }
}
