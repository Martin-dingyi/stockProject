package com.mdy.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author mdy
 * @date 2024-07-03 16:36
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermissionBO implements Serializable {
    /**
     * 权限id
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 前端显示的权限标题
     */
    private String title;

    /**
     * 权限使用的icon名称
     */
    private String icon;

    /**
     * 按钮名称
     */
    @JsonIgnore
    private String buttonName;

    /**
     * 权限路径
     */
    private String path;

    /**
     * 权限名
     */
    private String name;

    /**
     * 权限层级
     */
    @JsonIgnore
    private Integer type;

    /**
     * 权限的父权限id
     */
    @JsonIgnore
    private Long parentId;

    /**
     * 权限名
     */
    @JsonIgnore
    private String perms;

    /**
     * 子权限目录
     */
    private List<SysPermissionBO> children;
}
