package com.mdy.stock.mapper;

import com.mdy.stock.pojo.domain.StockUpdownDomain;
import com.mdy.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author martin
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查询所有涨幅榜数据
     * @param lastTime 最近交易时间
     * @return
     */
    List<StockUpdownDomain> findAll(@Param("curTime") Date lastTime);

    /**
     * 查询涨幅榜前四条数据，根据涨幅排序
     * @param lastTime 最近交易时间
     * @return
     */
    List<StockUpdownDomain> findFourUpDownData(@Param("curTime") Date lastTime);

    /**
     * 查询涨跌停股票的时间和数量
     * @param openTime 最近交易时间对应的开盘时间
     * @param lastTime 最近交易时间
     * @param flag 标志，代表是查询涨停还是跌停的数据
     * @return
     */
    List<Map> findUpDownCount(@Param("openTime") Date openTime,
                              @Param("endTime") Date lastTime, @Param("flag") int flag);
    
}
