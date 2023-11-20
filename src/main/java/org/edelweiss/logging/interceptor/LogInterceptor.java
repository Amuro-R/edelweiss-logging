package org.edelweiss.logging.interceptor;

import org.edelweiss.logging.aspect.LogConstant;
import org.edelweiss.logging.context.LogContext;
import org.edelweiss.logging.context.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.edelweiss.logging.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Log Interceptor preHandle");
        LogContext.initLogContext();
        String operator = userAuthService.isLogin() ? userAuthService.getUserName() : "default";
        String remoteAddress = IpUtil.getOriginIpFromHttpRequest(request);
        LogContext.setLogAttributeCommon(LogConstant.OPERATOR, operator);
        LogContext.setLogAttributeCommon(LogConstant.IP, remoteAddress);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogContext.removeLogContext();
    }


}
