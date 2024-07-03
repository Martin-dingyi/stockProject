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
 * @date 2024-04-16 9:40
 * @description 响应登录的JSON数据
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 该注解为一种设计模式的实现
public class RespLoginVo {
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

}
