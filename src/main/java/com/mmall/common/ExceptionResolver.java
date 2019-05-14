package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理全局异常
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        log.error("{} Exception", httpServletRequest.getRequestURI(), e);

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        // 当时用是jackson2.x的时候使用 MappingJackson2JsonView
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg", "接口异常, 详情请查看服务端的日志");
        modelAndView.addObject("data", e.toString());
        return modelAndView;
    }
}
