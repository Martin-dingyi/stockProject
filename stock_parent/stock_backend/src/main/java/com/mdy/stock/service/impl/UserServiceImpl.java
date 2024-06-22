package com.mdy.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.UserService;
import com.mdy.stock.utils.IdWorker;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mdy.stock.viewObject.response.ResponseCode.DATA_ERROR;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser getInfoByUsername(String name) {
        return sysUserMapper.findUserByUserName(name);
    }

    /**
     * 登录功能
     * @param reqLoginVo 请求信息包含用户名、密码和验证码
     * @return
     */
    @Override
    public R<RespLoginVo> login(ReqLoginVo reqLoginVo) {
        if (reqLoginVo == null || StringUtils.isBlank(reqLoginVo.getUsername())
                || StringUtils.isBlank(reqLoginVo.getPassword())
                || StringUtils.isBlank(reqLoginVo.getCode())
                || StringUtils.isBlank(reqLoginVo.getSessionId())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        RespLoginVo respLoginVo = new RespLoginVo();
        // 获取redis中存储的对应验证码，如果不存在或不一致则返回错误信息
        String redisCode = redisTemplate.opsForValue().get(reqLoginVo.getSessionId());
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(reqLoginVo.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        // 通过用户名查找用户信息
        SysUser sysUser = sysUserMapper.findUserByUserName(reqLoginVo.getUsername());
        if (sysUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        // 将输入的密码和数据库中的用户信息中的加密密码比对
        if (!passwordEncoder.matches(reqLoginVo.getPassword(), sysUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        BeanUtils.copyProperties(sysUser, respLoginVo);
        return R.ok(respLoginVo);
    }

    /**
     * 生成图形验证码和id，将id和验证码存入redis
     * @return 返回图片的base64格式数据和sessionId
     */
    @Override
    public R<Map> getCaptchaCode() {
        // 1.生成图形
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        // 2.生成四位验证码
        String captchaCode = lineCaptcha.getCode();
        // 3.生成唯一ID
        String sessionId = String.valueOf(idWorker.nextId());
        // 4.将id和验证码存入redis
        redisTemplate.opsForValue().set(sessionId, captchaCode, 1, TimeUnit.MINUTES);
        // 5.将图形转换为base64格式数据，并把图形和验证码封装进map
        HashMap<String, String> data = new HashMap<>();
        data.put("imageData", lineCaptcha.getImageBase64());
        data.put("sessionId", sessionId);
        return R.ok(data);
    }
}
