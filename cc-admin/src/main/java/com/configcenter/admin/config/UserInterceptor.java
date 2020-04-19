package com.configcenter.admin.config;

import com.configcenter.admin.entity.User;
import com.configcenter.admin.service.UserService;
import com.configcenter.admin.util.UserUtils;
import com.configcenter.common.util.ApiCode;
import com.configcenter.common.util.ApiResponse;
import com.configcenter.common.util.JsonUtil;
import com.configcenter.common.util.RedisOperation;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mjd on 2020/4/13 09:21
 */
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperation redisOperation;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json, charset=utf-8");
        String accessToken = request.getHeader("access-token");
        if (Strings.isNullOrEmpty(accessToken)) {
            response.getWriter().write(JsonUtil.toJsonStr(ApiResponse.error(ApiCode.UNAUTHORISED)));
            return false;
        }
        Integer uId = redisOperation.getInteger(accessToken);
        if (uId == null || uId <= 0) {
            response.getWriter().write(JsonUtil.toJsonStr(ApiResponse.error(ApiCode.UNAUTHORISED)));
            return false;
        }
        User user = userService.getUserById(uId);
        if (user == null) {
            response.getWriter().write(JsonUtil.toJsonStr(ApiResponse.error(ApiCode.UNAUTHORISED)));
            return false;
        }
        UserUtils.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtils.clear();
    }
}
