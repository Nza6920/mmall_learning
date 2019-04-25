package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 新建收货地址
     * @param session  会话
     * @param shipping 收货地址
     * @return ServerResponse
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping)
    {
        // 判断权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("未登录!");
        }

        return iShippingService.add(user.getId(), shipping);
    }


    /**
     * 删除收货地址
     * @param session    会话
     * @param shippingId 收货地址的id
     * @return ServerResponse
     */
    @RequestMapping(value = "del.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId)
    {
        // 判断权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("未登录!");
        }

        return iShippingService.del(user.getId(), shippingId);
    }

    /**
     * 更新收货地址
     * @param session    会话
     * @param shipping 收货地址
     * @return ServerResponse
     */
    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping)
    {
        // 判断权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("未登录!");

        }

        return iShippingService.update(user.getId(), shipping);
    }

    /**
     * 查询收货地址信息
     * @param session     会话
     * @param shippingId  收货地址id
     * @return ServerResponse
     */
    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId)
    {
        // 判断权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("未登录!");
        }

        return iShippingService.select(user.getId(), shippingId);
    }


    /**
     * 查询用户的收货地址列表
     * @param pageNum  起始页
     * @param pageSize 每一页的数据量
     * @param session  会话
     * @return ServerResponse<PageInfo>
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpSession session)
    {
        // 判断权限
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("未登录!");
        }

        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
