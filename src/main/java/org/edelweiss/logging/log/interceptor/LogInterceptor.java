package org.edelweiss.logging.log.interceptor;

import org.edelweiss.logging.log.aspect.LogOperationConstant;
import org.edelweiss.logging.log.context.LogContext;
import org.edelweiss.logging.log.context.UserAuthContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Log Interceptor preHandle");
        LogContext.initLogContext();
        String operator = null;
        String phone = null;
        if (UserAuthContextHolder.isLogin()) {
            operator = UserAuthContextHolder.getUserName();
            phone = UserAuthContextHolder.getPhone();
        }
        String remoteAddress = request.getRemoteAddr();
        LogContext.setLogAttributeCommon(LogOperationConstant.OPERATOR, operator);
        LogContext.setLogAttributeCommon(LogOperationConstant.IP, remoteAddress);
        LogContext.setLogAttributeCommon(LogOperationConstant.PHONE, phone);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogContext.removeLogContext();
    }


}
