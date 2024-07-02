package com.mdy.stock.pojo.domain;

import com.mdy.stock.pojo.entity.SysRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mdy
 * @date 2024-07-03 1:36
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleBO {
    /**
     * 拥有者的id
     */
    private List<Long> ownRoleIds;

    /**
     * 所有用户角色信息
     */
    private List<SysRole> allRole;
}
