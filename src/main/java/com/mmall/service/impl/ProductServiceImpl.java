package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "iProductServiceImpl")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper = null;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            // 判断子图是否为空
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    // 设置主图为子图中的第一个
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                // 更新产品
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功!");
                }
                return ServerResponse.createByErrorMessage("更新产品失败!");
            } else {
                // 新增产品
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功!");
                }
                return ServerResponse.createByErrorMessage("新增产品失败!");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确!");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        product.setUpdateTime(new Date(System.currentTimeMillis()));

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }

        return ServerResponse.createByErrorMessage("修改产品销售状态失败!");
    }
}
