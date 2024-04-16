package com.mdy.stock.service;

import com.mdy.stock.mapper.SysUserMapper;
import com.mdy.stock.pojo.entity.SysUser;
import com.mdy.stock.viewObject.request.ReqLoginVo;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.RespLoginVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description
 */

public interface UserService {
    SysUser getInfoByUsername(String name);

    R<RespLoginVo> login(ReqLoginVo reqLoginVo);
}
