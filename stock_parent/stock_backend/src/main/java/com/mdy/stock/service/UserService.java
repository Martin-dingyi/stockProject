package com.mdy.stock.service;

import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import org.springframework.beans.factory.annotation.Autowired;

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
    SysUser getInfoByUsername(String name);

    /**
     * 根据用户名、密码和验证码验证登录信息
     * @param reqLoginVo 保持登录信息的对象
     * @return
     */
    R<RespLoginVo> login(ReqLoginVo reqLoginVo);

    /**
     * 获取验证码
     * @return
     */
    R<Map> getCaptchaCode();
}
