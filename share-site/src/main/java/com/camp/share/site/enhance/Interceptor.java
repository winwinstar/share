package com.camp.share.site.enhance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wushengsheng on 2017/6/15.
 */
@Slf4j
public class Interceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String appName = httpServletRequest.getHeader("AppName");
        String appVersion = httpServletRequest.getHeader("AppVersion");
        log.info("Request: {}, Referer: {}" , httpServletRequest.getRequestURI(), httpServletRequest.getHeader("Referer"));
        log.info("userAgent=" + userAgent + ",appName" + appName + ",appVersion=" + appVersion);

        if (!httpServletResponse.containsHeader("Access-Control-Allow-Origin")) {
            httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        }
        if (!httpServletResponse.containsHeader("Access-Control-Allow-Credentials")) {
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        }
        if (!httpServletResponse.containsHeader("Access-Control-Allow-Methods")) {
            httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
