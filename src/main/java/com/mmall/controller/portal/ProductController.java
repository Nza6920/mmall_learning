package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/product")
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
     * 查看产品详情
     * @param productId 产品 id
     * @return ServerResponse
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailRESTful(@PathVariable Integer productId)
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


    /**
     * 前台商品列表
     * @param keyWord 关键字
     * @param categoryId 分类 id
     * @param pageNum 当前页数
     * @param pageSize 每一页的数据量
     * @param orderBy 排序
     * @return ServerResponse
     */
    // http://localhost:9088/product/手机/100012/1/10/price_asc
    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(
            @PathVariable(value = "keyword")String keyWord,
            @PathVariable(value = "categoryId")Integer categoryId,
            @PathVariable(value = "pageNum") Integer pageNum,
            @PathVariable(value = "pageSize")Integer pageSize,
            @PathVariable(value = "orderBy")String orderBy)
    {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageNum = 10;
        }
        if(StringUtils.isBlank(orderBy)) {
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeyWordCategory(keyWord, categoryId, pageNum, pageSize, orderBy);
    }

    /**
     * 前台商品列表
     * @param categoryId 分类 id
     * @param pageNum 当前页数
     * @param pageSize 每一页的数据量
     * @param orderBy 排序
     * @return ServerResponse
     */
    // http://localhost:9088/product/100012/1/10/price_asc
    @RequestMapping(value = "/category/{categoryId}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(
            @PathVariable(value = "categoryId")Integer categoryId,
            @PathVariable(value = "pageNum") Integer pageNum,
            @PathVariable(value = "pageSize")Integer pageSize,
            @PathVariable(value = "orderBy")String orderBy)
    {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageNum = 10;
        }
        if(StringUtils.isBlank(orderBy)) {
            orderBy = "price_asc";
        }
        return iProductService.getProductByKeyWordCategory("", categoryId, pageNum, pageSize, orderBy);
    }

    /**
     * 前台商品列表
     * @param keyWord 关键字
     * @param pageNum 当前页数
     * @param pageSize 每一页的数据量
     * @param orderBy 排序
     * @return ServerResponse
     */
    // http://localhost:9088/product/手机/1/10/price_asc
    @RequestMapping(value = "/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(
            @PathVariable(value = "keyword")String keyWord,
            @PathVariable(value = "pageNum") Integer pageNum,
            @PathVariable(value = "pageSize")Integer pageSize,
            @PathVariable(value = "orderBy")String orderBy)
    {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageNum = 10;
        }
        if(StringUtils.isBlank(orderBy)) {
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeyWordCategory(keyWord, null, pageNum, pageSize, orderBy);
    }
}
