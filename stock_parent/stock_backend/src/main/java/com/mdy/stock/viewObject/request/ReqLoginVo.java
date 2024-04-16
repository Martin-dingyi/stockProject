package com.mdy.stock.viewObject.request;

import lombok.Data;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description
 */

@Data
public class ReqLoginVo {
    /*
    * 用户名
    * */
    private String username;
    /*
     * 密码
     * */
    private String password;
    /*
     * 验证码
     * */
    private String code;
}
