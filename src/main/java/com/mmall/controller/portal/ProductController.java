package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 查看产品详情
     * @param productId 产品 id
     * @return ServerResponse
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId)
    {
        return iProductService.getProductDetail(productId);
    }


    /**
     * 前台商品列表
     * @param keyWord 关键字
     * @param categoryId 分类 id
     * @param pageNum 当前页数
     * @param pageSize 每一页的数据量
     * @param orderBy 排序
     * @return ServerResponse
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(
            @RequestParam(value = "keyword", required = false)String keyWord,
            @RequestParam(value = "categoryId", required = false)Integer categoryId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "")String orderBy)
    {
        return iProductService.getProductByKeyWordCategory(keyWord, categoryId, pageNum, pageSize, orderBy);
    }
}
