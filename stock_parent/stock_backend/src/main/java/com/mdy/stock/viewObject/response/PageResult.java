package com.mdy.stock.viewObject.response;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mdy
 * @date 2024-06-21 0:14
 * @description
 */
@Data
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long totalRows;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 当前在第几页
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 当前页记录数
     */
    private Integer curPageSize;

    /**
     * 结果集
     */
    private List<T> rows;


    /**
     * 分页数据组装
     * @param pageInfo 分页对象
     * @return
     */
    public PageResult(PageInfo<T> pageInfo) {
        totalRows = pageInfo.getTotal();
        totalPages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        curPageSize = pageInfo.getSize();
        rows = pageInfo.getList();
    }
}
