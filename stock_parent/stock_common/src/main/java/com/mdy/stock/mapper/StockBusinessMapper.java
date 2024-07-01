package com.mdy.stock.mapper;

import com.mdy.stock.pojo.domain.StockBusinessBO;
import com.mdy.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

/**
* @author martin
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-04-11 17:03:13
* @Entity com.mdy.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);


    /**
     * 根据编码获取个股商业信息
     * @param code 编码
     * @return R
     */
    StockBusinessBO findStockBusinessInfoByCode(@Param("code") String code);
}
