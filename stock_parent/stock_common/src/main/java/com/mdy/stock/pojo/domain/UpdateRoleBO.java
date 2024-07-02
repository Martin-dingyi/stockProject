package com.mdy.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mdy
 * @date 2024-07-03 3:14
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleBO {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 该用户要更新的角色ids
     */
    private List<Long> roleIds;
}
