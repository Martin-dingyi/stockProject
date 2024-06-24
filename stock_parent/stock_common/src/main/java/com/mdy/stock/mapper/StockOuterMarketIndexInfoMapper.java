package com.mdy.stock.mapper;

import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import com.mdy.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author martin
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    /**
     * 批量导入国内大盘指数信息
     * @param stockMarketIndexInfos 大盘指数信息列表
     */
    void insertStockInfosPatch(@Param("stockMarketIndexInfos") List<StockMarketIndexInfo> stockMarketIndexInfos);
}
