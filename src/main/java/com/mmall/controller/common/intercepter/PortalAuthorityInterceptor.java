package com.mmall.controller.common.intercepter;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 前台权限检查拦截器
 */
@Slf4j
public class PortalAuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");

        // 请求 controller 中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 解析handlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        // 解析参数
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();

        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }

            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        // 如果是 UserController 放行
        if (StringUtils.equals(className, "UserController")) {
            // 如果 UserController 请求, 不打印参数, 因为参数里面可能含有密码
            log.info("权限拦截器拦截到请求, className: {}, methodName: {}", className, methodName);
            return true;
        }

        log.info("权限拦截器拦截到请求, className: {}, methodName: {}, parm: {}", className, methodName, requestParamBuffer.toString());

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }

        // 用户未登录
        if (user == null) {
            response.reset();                                               // 重置response
            response.setCharacterEncoding("UTF-8");                         // 设置编码, 否则会乱码
            response.setContentType("application/json;charset=UTF-8");      // 设置返回值的类型

            PrintWriter out = response.getWriter();
            try {
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc())));
            } finally {
                out.flush();
                out.close();    // 关闭out流
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("postHandle");
    }
}
