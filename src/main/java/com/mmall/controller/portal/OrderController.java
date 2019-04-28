package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IOrderService iOrderService = null;


    /**
     * 创建订单
     * @param session     会话
     * @param shippingId  收货地址
     * @return ServerResponse
     */
    @RequestMapping(value = "create.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.createOrder(user.getId(), shippingId);
    }

    /**
     * 发起支付
     * @param session 会话
     * @param orderNo 订单号
     * @param request 请求
     * @return ServerResponse
     */
    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 支付宝回调
     * @param request 请求参数
     * @return Object
     */
    @RequestMapping(value = "alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request)
    {
        logger.info("进入回调");

        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();

        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                // 用逗号拼接
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                params.put(name, valueStr);
            }
        }

        logger.info("支付宝回调, sign:{}, trade_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 1.验证回调的正确性是不是支付宝发的, 2. 避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (! alipayRSACheckedV2) {
                // 业务逻辑
                return ServerResponse.createByErrorMessage("非法请求, 验证不通过.");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝验证回调异常", e);
        }

        // todo 验证各种数据

        ServerResponse serverResponse = iOrderService.aliCallback(params);

        if (serverResponse.isSuccess()) {
            return Const.AliPayCallback.RESPONSE_SUCCESS;
        }

        return Const.AliPayCallback.RESPONSE_FAILED;
    }

    /**
     * 查询订单状态
     * @param session 会话
     * @param orderNo 订单号
     * @return ServerResponse<Boolean>
     */
    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo)
    {
        // 判断用户是否登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);

        return serverResponse.isSuccess() ? ServerResponse.createBySuccess(true) : ServerResponse.createBySuccess(false);
    }

}
