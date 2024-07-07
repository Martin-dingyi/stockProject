package com.mdy.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdy.stock.mapper.SysPermissionMapper;
import com.mdy.stock.mapper.SysRoleMapper;
import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.mapper.SysUserRoleMapper;
import com.mdy.stock.pojo.domain.RoleBO;
import com.mdy.stock.pojo.domain.UpdateRoleBO;
import com.mdy.stock.pojo.domain.SysPermissionBO;
import com.mdy.stock.pojo.entity.SysRole;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.service.UserService;
import com.mdy.stock.utils.IdWorker;
import com.mdy.stock.viewObject.request.ReqListRoleVO;
import com.mdy.stock.viewObject.request.ReqListUserVO;
import com.mdy.stock.viewObject.request.ReqLoginVO;
import com.mdy.stock.viewObject.response.PageResult;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author martin
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser findUserByName(String name) {
        return sysUserMapper.findUserByUserName(name);
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

    /**
     * 根据分页参数查询用户数据
     *
     * @param reqListUserVO 前端传来的json数据
     * @return PageResult
     */
    @Override
    public R<PageResult<SysUser>> listUsers(ReqListUserVO reqListUserVO) {
        int pageNum = reqListUserVO.getPageNum();
        int pageSize = reqListUserVO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        List<SysUser> users = sysUserMapper.findUserByStartAndEndTime(reqListUserVO.getStartTime(),
                reqListUserVO.getEndTime(),
                reqListUserVO.getUsername(),
                reqListUserVO.getNickName());
        PageInfo<SysUser> pageUserInfo = new PageInfo<>(users);

        return R.ok(new PageResult<>(pageUserInfo));
    }

    @Override
    public boolean insertUser(SysUser user) {
        user.setId(idWorker.nextId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setDeleted(1);
        return sysUserMapper.insert(user) > 0;
    }

    /**
     * 根据分页信息查询用户角色信息
     * @return R
     */
    @Override
    public R<PageResult<SysRole>> listSysRoles(ReqListRoleVO reqListRoleVO) {
        PageHelper.startPage(reqListRoleVO.getPageNum(), reqListRoleVO.getPageSize());

        List<SysRole> sysRoles = sysRoleMapper.findAll();
        PageInfo<SysRole> pageInfo = new PageInfo<>(sysRoles);

        return R.ok(new PageResult<>(pageInfo));
    }

    /**
     * 根据多个id批量删除用户信息
     * @param ids 存储待删除用户的id
     * @return 返回执行结果
     */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        return sysUserMapper.deleteByIds(ids) > 0;
    }

    /**
     * 根据用户id获取关于他的所有角色的信息
     * @param userId 用户id
     * @return R
     */
    @Override
    public R<RoleBO> getRolesById(Long userId) {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(userId);
        // 组装roleBO
        RoleBO roleBO = new RoleBO();
        roleBO.setOwnRoleIds(roleIds);
        roleBO.setAllRole(sysRoleMapper.findRolesById(userId));
        return R.ok(roleBO);
    }

    /**
     * 根据id修改它的角色信息
     * @param updateRoleBO 保持角色id和要改变的角色ids
     * @return 操作结果
     */
    @Override
    public boolean updateRolesById(UpdateRoleBO updateRoleBO) {
        List<Long> ids = updateRoleBO.getRoleIds();
        Long userId = updateRoleBO.getUserId();

        // 先删除用户对应的角色，然后再向表中添加新的对应关系
        sysUserRoleMapper.deleteByUsrId(userId);
        for (Long id : ids) {
            if (sysUserRoleMapper.insertUserRoles(idWorker.nextId(), updateRoleBO.getUserId(), id, new Date()) < 0) {
                return false;
            }
        }
        return true;
    }
}
