package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService = null;

    @Autowired
    private IOrderService iOrderService = null;

    /**
     * 后台管理员获取订单列表
     * @param request   HttpServletRequest
     * @param pageNum   页数
     * @param pageSize  每一页的数据量
     * @return ServerResponse<PageInfo>
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest request,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)
    {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录, 无法获取信息.");
        }
        // json -> user
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }

        // 校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iOrderService.manageList(pageNum, pageSize);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    /**
     * 获取订单详情
     * @param request HttpServletRequest
     * @param orderNo 订单详情
     * @return ServerResponse<PageInfo>
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo)
    {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录, 无法获取信息.");
        }
        // json -> user
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }

        // 校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iOrderService.manageDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    /**
     * 查找订单
     * @param request HttpServletRequest
     * @param orderNo 订单号
     * @return ServerResponse
     */
    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest request,
                                               Long orderNo,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize)
    {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录, 无法获取信息.");
        }
        // json -> user
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }

        // 校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }

    /**
     * 订单发货
     * @param request HttpServletRequest
     * @param orderNo 订单详情
     * @return ServerResponse<PageInfo>
     */
    @RequestMapping(value = "send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo)
    {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录, 无法获取信息.");
        }
        // json -> user
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }

        // 校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iOrderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作, 需要管理员权限");
        }
    }
}
