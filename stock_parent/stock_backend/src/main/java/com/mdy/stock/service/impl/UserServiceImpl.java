package com.mdy.stock.service.impl;

import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.UserService;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mdy.stock.viewObject.response.ResponseCode.DATA_ERROR;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser getInfoByUsername(String name) {
        return sysUserMapper.findUserByUserName(name);
    }

    @Override
    public R<RespLoginVo> login(ReqLoginVo reqLoginVo) {
        if (reqLoginVo == null || reqLoginVo.getUsername() == null || reqLoginVo.getPassword() == null || reqLoginVo.getCode() == null) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        // 通过用户名查找用户信息
        SysUser sysUser = sysUserMapper.findUserByUserName(reqLoginVo.getUsername());
        if (sysUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        RespLoginVo respLoginVo = new RespLoginVo();
        // 将输入的密码和数据库中的用户信息中的加密密码比对
        // 1.如果相同，返回用户信息；2.如果不同，返回错误信息
        if (passwordEncoder.matches(reqLoginVo.getPassword(), sysUser.getPassword())) {
            BeanUtils.copyProperties(sysUser, respLoginVo);
            return R.ok(respLoginVo);
        }
        return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
    }
}
