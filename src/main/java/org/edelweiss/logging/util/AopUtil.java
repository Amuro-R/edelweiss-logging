package org.edelweiss.logging.util;

import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;

/**
 * @author jingyun
 * @date 2022-10-26
 */
public class AopUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(T origin) {
        if (AopUtils.isAopProxy(origin)) {
            return (T) AopContext.currentProxy();
        }
        return origin;
    }

}
