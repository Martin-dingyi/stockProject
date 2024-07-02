package com.mdy.stock.viewObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mdy
 * @date 2024-07-02 21:29
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqListUserVO {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 用戶名
     */
    private String username;

    /**
     * 别名
     */
    private String nickName;

    /**
     * 创建时间
     */
    private String startTime;

    /**
     * 最近更新时间
     */
    private String endTime;
}
