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

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

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
     * 组装menus，type的值决定本次递归组装的层级
     * @param permissions 权限集合
     * @param type 当前应组装的权限的层级
     * @return 返回组装好的menus
     */
    private List<SysPermissionBO> composeMenus(List<SysPermissionBO> permissions, List<SysPermissionBO> menus, Integer type) {
        if (type >= 3) {
            return menus;
        }

        // 用一个map记录多个parentId和type都相同的permission
        Map<Long, List<SysPermissionBO>> childrenMap = new HashMap<>();
        for (SysPermissionBO permission : permissions) {
            if (Objects.equals(permission.getType(), type)) {
                permission.setChildren(new ArrayList<>());
                if (childrenMap.containsKey(permission.getParentId())) {
                    childrenMap.get(permission.getParentId()).add(permission);
                } else {
                    List<SysPermissionBO> children = new ArrayList<>();
                    children.add(permission);
                    childrenMap.put(permission.getParentId(), children);
                }
            }
        }

        // 将组装好的子集合，按照parentId分别插入到对应的父结点中
        if (menus.isEmpty()) {
            menus.addAll(childrenMap.get(0L));
        } else {
            for (SysPermissionBO menu : menus) {
                // childrenMap中有和menu的id相等的key，说明该键值对的键值是这个menu的子集
                if (childrenMap.containsKey(menu.getId())) {
                    menu.setChildren(childrenMap.get(menu.getId()));
                }
            }
        }

        return composeMenus(permissions, menus, type + 1);
    }

    /**
     * 登录功能
     * @param reqLoginVO 请求信息包含用户名、密码和验证码
     * @return 登录成功或失败信息
     */
    @Override
    public R<RespLoginVo> login(ReqLoginVO reqLoginVO) {
        if (reqLoginVO == null || StringUtils.isBlank(reqLoginVO.getUsername())
                || StringUtils.isBlank(reqLoginVO.getPassword())
                || StringUtils.isBlank(reqLoginVO.getCode())
                || StringUtils.isBlank(reqLoginVO.getSessionId())) {
            return R.error(ResponseCode.DATA_ERROR);
        }

        RespLoginVo respLoginVo = new RespLoginVo();
        // 获取redis中存储的对应验证码，如果不存在或不一致则返回错误信息
        String redisCode = redisTemplate.opsForValue().get(reqLoginVO.getSessionId());
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(reqLoginVO.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }

        // 通过用户名查找用户信息，如果用户不存在则返回错误信息。
        SysUser sysUser = sysUserMapper.findUserByUserName(reqLoginVO.getUsername());
        if (sysUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }

        // 将输入的密码和数据库中的用户信息中的加密密码比对，如果不一致则报错。
        if (!passwordEncoder.matches(reqLoginVO.getPassword(), sysUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 获取用户所拥有的权限集合
        List<SysPermissionBO> sysPermissions = sysPermissionMapper.findUserPermissions(sysUser.getId());
        // 将权限按照层级顺序组装到ResLoginVO的children属性中
        List<SysPermissionBO> menus = new ArrayList<>();
        // 形成菜单栏层级
        composeMenus(sysPermissions, menus, 1);

        // 组装按钮
        List<String> buttonPermissions = new ArrayList<>();
        for (SysPermissionBO sysPermission : sysPermissions) {
            if (Objects.equals(sysPermission.getType(), 3)) {
                buttonPermissions.add(sysPermission.getButtonName());
            }
        }

        // 组装respLoginVo
        // BeanUtils.copyProperties方法将sysUser的属性值复制到respLoginVo中。
        BeanUtils.copyProperties(sysUser, respLoginVo);
        respLoginVo.setMenus(menus);
        respLoginVo.setPermissions(buttonPermissions);
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
