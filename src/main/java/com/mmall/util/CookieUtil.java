package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = "happymmall.test";  // Tomcat 8.5 以上的不要加点
    private final static String COOKIE_NAME = "mmall_login_token";

    /**
     * 写入 cookie
     * @param response HttpServletResponse
     * @param token cookie 的值
     */
    public static void writeLoginToken(HttpServletResponse response, String token)
    {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");                         // 代表设置在根目录
        ck.setHttpOnly(true);                    // 不许通过脚本读取
        ck.setMaxAge(60 * 60 * 24 * 365);        // 单位:s  -1 永久有效 0 删除cookie, 如果不赋值,则不会写入硬盘, 只在当前页面有效
        log.info("write cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    /**
     * 读取 cookie
     * @param request HttpServletRequest
     * @return String
     */
    public static String readLoginToken(HttpServletRequest request)
    {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                log.info("read cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("return cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }

        return null;
    }

    /**
     * 删除 cookie
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cks = request.getCookies();

        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);       // 设置 0 为删除 cookie
                    log.info("del cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());

                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
