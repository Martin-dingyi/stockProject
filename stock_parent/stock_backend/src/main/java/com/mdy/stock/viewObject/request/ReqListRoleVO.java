package com.mdy.stock.viewObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mdy
 * @date 2024-07-03 1:53
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqListRoleVO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;
}
