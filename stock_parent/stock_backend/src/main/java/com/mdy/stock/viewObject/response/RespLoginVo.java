package com.mdy.stock.viewObject.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    /*
    * 用户名
    * */
    private String username;
    /*
     * 用户昵称
     * */
    private String nickname;
    /*
    * 电话号码
    * */
    private String phone;
}
