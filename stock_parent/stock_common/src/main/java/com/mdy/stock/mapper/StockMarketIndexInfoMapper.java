package com.mdy.stock.mapper;

import com.mdy.stock.pojo.domain.InnerMarketDomain;
import com.mdy.stock.pojo.domain.InnerSectorDomain;
import com.mdy.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author martin
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {
    /**
     * 根据最近交易时间和国内大盘编码查询所有国内大盘数据
     * @param lastTime
     * @param innerMarketCodes
     * @return
     */
    List<InnerMarketDomain> getInnerMarketInfo(@Param("cur_time") Date lastTime, @Param("marketCodes") List<String> innerMarketCodes);

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    List<InnerSectorDomain> getInnerMarketSectorInfo(@Param("cur_time") Date lastTime);
}
