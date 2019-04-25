package com.mmall.dao;

import com.google.common.collect.Lists;
import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdAndUserId(@Param("userId")Integer userId, @Param("shippingId") Integer shippingId);

    int updateByShipping(Shipping record);

    Shipping selectByShippingIdAndUserId(@Param("userId")Integer userId, @Param("shippingId") Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);
}