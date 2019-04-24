package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加商品到购物车
     * @param session   会话
     * @param count     数量
     * @param productId 商品id
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 更新购物车信息
     * @param session   会话
     * @param count     数量
     * @param productId 产品id
     * @return  ServerResponse<CartVo>
     */
    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 删除购物车中的产品
     * @param session    会话
     * @param productIds 产品 ids
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "delete_product.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * 购物车中的所有产品
     * @param session 会话
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.list(user.getId());
    }

    /**
     * 全选
     * @param session 会话
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    /**
     * 全反选
     * @param session 会话
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "un_select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UNCHECKED);
    }

    /**
     * 取消单选
     * @param  session 会话
     * @param  productId 产品 ID
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "un_select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession session, Integer productId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UNCHECKED);
    }

    /**
     * 单独选
     * @param session   会话
     * @param productId 产品ID
     * @return ServerResponse<CartVo>
     */
    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session, Integer productId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    /**
     * 获取当前用户购物车中产品总数
     * @param session 会话
     * @return ServerResponse<Integer>
     */
    @RequestMapping(value = "get_cart_product_count.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }

        return iCartService.getCartProductCount(user.getId());
    }

}
