package com.mdy.stock.viewObject.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mdy.stock.pojo.domain.SysPermissionBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mdy
 * @date 2024-07-07 6:04
 * @description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVOExt {

    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 权限目录
     */
    private List<SysPermissionBO> menus;

    /**
     * 权限按钮标识
     */
    private List<String> permissions;

    /**
     * Token
     */
    private String accessToken;


}
