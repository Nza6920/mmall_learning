package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService = null;

    /**
     * 后台管理员登录
     * @param username  用户名
     * @param password  密码
     * @param session   会话
     * @return ServerResponse
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse resp)
    {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            User user = response.getData();

            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 登陆的是管理员
                CookieUtil.writeLoginToken(resp, session.getId());    // 写入 cookie
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员, 无法登陆!");
            }
        }
        return response;
    }
}
