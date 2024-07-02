package com.mdy.stock.service;

import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.viewObject.request.ReqListUserVO;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;

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

    /**
     * 根据分页参数查询用户数据
     *
     * @param reqListUserVO 前端传来的json数据
     * @return PageResult
     */
    R<PageResult<SysUser>> listUsers(ReqListUserVO reqListUserVO);

    /**
     * 添加用户信息
     * @param user 接受用户数据
     * @return 操作成功与否信息
     */
    boolean insertUser(SysUser user);
}
