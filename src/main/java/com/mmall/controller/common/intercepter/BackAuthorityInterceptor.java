package com.mmall.controller.common.intercepter;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
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
 * 后台权限检查拦截器
 */
@Slf4j
public class BackAuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 解析HandlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        // 解析参数, 具体的参数key以及value是什么
        StringBuffer requestParamBuffer = new StringBuffer();    // 用来打印日志
        Map paramMap = request.getParameterMap();

        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();

            String mapValue = StringUtils.EMPTY;

            // request这个参数的map, 里面的value是一个String[]
            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }

            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        // 放行登陆请求
        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
            // 如果拦截到登陆请求, 不打印参数, 因为参数里面有密码
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
        // 用户未登录 或 不是管理员
        if (user == null || user.getRole().intValue() != Const.Role.ROLE_ADMIN) {
            response.reset();                                               // 重置response
            response.setCharacterEncoding("UTF-8");                         // 设置编码, 否则会乱码
            response.setContentType("application/json;charset=UTF-8");      // 设置返回值的类型

            PrintWriter out = response.getWriter();
            try {
                if (user == null) {
                    // 处理富文本上传
                    if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                        Map resultMap = Maps.newHashMap();
                        resultMap.put("success", false);
                        resultMap.put("msg", "请登录管理员");
                        out.print(JsonUtil.obj2String(resultMap));
                    }else {
                        out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));
                    }
                } else {
                    // 处理富文本上传
                    if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                        Map resultMap = Maps.newHashMap();
                        resultMap.put("success", false);
                        resultMap.put("msg", "无权限操作");
                        out.print(JsonUtil.obj2String(resultMap));
                    }else {
                        out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户无权限操作")));
                    }
                }
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
//        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        log.info("afterCompletion");
    }
}
