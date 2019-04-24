package com.mmall.dao;

import com.google.common.collect.Lists;
import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(@Param(value = "userId") Integer userId,
                                        @Param(value = "productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param(value = "userId") Integer userId,
                                    @Param(value = "productIdList") List<String> productIdList);

    int checkedOrUnCheckedProduct(@Param("userId") Integer userId,
                                  @Param(value = "productId") Integer productId,
                                  @Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);
}